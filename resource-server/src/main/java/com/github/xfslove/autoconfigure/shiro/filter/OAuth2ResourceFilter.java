package com.github.xfslove.autoconfigure.shiro.filter;

import com.github.xfslove.autoconfigure.shiro.exception.OAuth2AuthenticationException;
import com.github.xfslove.autoconfigure.shiro.jwt.JwtUtils;
import com.github.xfslove.autoconfigure.shiro.model.Constants;
import com.github.xfslove.autoconfigure.shiro.model.Jwt;
import com.github.xfslove.autoconfigure.shiro.realm.OAuth2ResourceToken;
import org.apache.commons.lang3.StringUtils;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by hanwen on 2017/9/20.
 */
public class OAuth2ResourceFilter extends AuthenticatingFilter {

  private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2ResourceFilter.class);

  private String serverScheme;

  private String serverHost;

  private int serverPort;

  private String serverContextPath;

  private String clientId;

  private String clientSecret;

  @Override
  protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
    Subject subject = SecurityUtils.getSubject();
    Session session = subject.getSession(false);
    Jwt jwt = (Jwt) session.getAttribute(OAuth.OAUTH_ACCESS_TOKEN);
    session.removeAttribute(OAuth.OAUTH_ACCESS_TOKEN);
    return new OAuth2ResourceToken(jwt);
  }

  @Override
  protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
    Subject subject = SecurityUtils.getSubject();
    if (!subject.isAuthenticated()) {
      return false;
    }

    Jwt jwt = (Jwt) subject.getPrincipals().asList().get(1);
    return jwt.getExpires().getTime() >= System.currentTimeMillis();
  }

  @Override
  protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
    HttpServletRequest httpRequest = WebUtils.toHttp(request);
    Subject subject = SecurityUtils.getSubject();

    if (subject.isAuthenticated()) {
      // refresh token
      Jwt jwt = (Jwt) subject.getPrincipals().asList().get(1);

      try {
        refreshToken(request, (String) jwt.getClaims().get(Constants.REFRESH_TOKEN));
        return executeLogin(request, response);
      } catch (OAuth2AuthenticationException e) {
        LOGGER.error("UAA RESOURCE INFO : oauth resource refresh token error. msg:{}", e.getMessage());
        subject.logout();
        redirectToLogin(request, response);
        return false;
      }
    } else {
      // access token
      String error = httpRequest.getParameter(OAuthError.OAUTH_ERROR);
      if (StringUtils.isNotBlank(error)) {
        String description = httpRequest.getParameter(OAuthError.OAUTH_ERROR_DESCRIPTION);
        LOGGER.error("UAA RESOURCE INFO : oauth resource get auth_code error. msg:{}", description);
        redirectToLogin(request, response);
        return false;
      }

      String authCode = httpRequest.getParameter(OAuth.OAUTH_CODE);
      Session session = subject.getSession(false);
      Jwt jwt = session == null ? null : (Jwt) session.getAttribute(OAuth.OAUTH_ACCESS_TOKEN);

      if (StringUtils.isBlank(authCode) && jwt == null) {

        redirectToLogin(request, response);
        return false;
      }

      if (jwt == null) {
        try {
          saveToken(request, authCode);
        } catch (OAuth2AuthenticationException e) {
          LOGGER.error("UAA RESOURCE INFO : oauth resource get access_token error. msg:{}", e.getMessage());
          redirectToLogin(request, response);
          return false;
        }

        String withoutCodeUrl = UriComponentsBuilder
            .fromHttpUrl(httpRequest.getRequestURL().toString() + "?" + httpRequest.getQueryString())
            .replaceQueryParam(OAuth.OAUTH_CODE).build()
            .toUriString();
        WebUtils.issueRedirect(request, response, withoutCodeUrl);
        return false;
      }

      return executeLogin(request, response);
    }
  }

  @Override
  protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
    try {

      redirectToLogin(request, response);
      return false;
    } catch (IOException ex) {
      throw new OAuth2AuthenticationException(ex);
    }
  }

  @Override
  protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
    String loginUrl = getLoginUrl(request);
    LOGGER.info("UAA RESOURCE INFO : redirect to login url[{}]", loginUrl);
    WebUtils.issueRedirect(request, response, loginUrl);
  }

  private String getLoginUrl(ServletRequest request) {
    HttpServletRequest httpRequest = WebUtils.toHttp(request);
    String redirectUrl = httpRequest.getRequestURL().toString();
    if (StringUtils.isNotBlank(httpRequest.getQueryString())) {
      redirectUrl += "?" + httpRequest.getQueryString();
    }

    String authCodeUrl = UriComponentsBuilder.newInstance()
        .scheme(serverScheme)
        .host(serverHost)
        .port(serverPort)
        .path(serverContextPath + Constants.SERVER_AUTHORIZE_PATH)
        .build().toUriString();

    try {
      OAuthClientRequest authCodeRequest = OAuthClientRequest
          .authorizationLocation(authCodeUrl)
          .setClientId(clientId)
          .setResponseType(OAuth.OAUTH_CODE)
          .setRedirectURI(redirectUrl)
          .buildQueryMessage();

      return authCodeRequest.getLocationUri();
    } catch (OAuthSystemException e) {
      throw new OAuth2AuthenticationException();
    }
  }

  private void saveToken(ServletRequest request, String authCode) {
    HttpServletRequest httpRequest = WebUtils.toHttp(request);

    Subject subject = SecurityUtils.getSubject();
    Session session = subject.getSession();

    String accessTokenUrl = UriComponentsBuilder.newInstance()
        .scheme(serverScheme)
        .host(serverHost)
        .port(serverPort)
        .path(serverContextPath + Constants.SERVER_ACCESS_TOKEN_PATH)
        .build().toUriString();

    String redirectUrl = httpRequest.getRequestURL().toString();
    if (StringUtils.isNotBlank(httpRequest.getQueryString())) {
      redirectUrl += "?" + httpRequest.getQueryString();
    }

    try {
      OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
      // get access_token
      OAuthClientRequest req = OAuthClientRequest
          .tokenLocation(accessTokenUrl)
          .setGrantType(GrantType.AUTHORIZATION_CODE)
          .setClientId(clientId)
          .setClientSecret(clientSecret)
          .setRedirectURI(redirectUrl)
          .setCode(authCode)
          .buildQueryMessage();

      OAuthAccessTokenResponse resp = oAuthClient.accessToken(req, OAuth.HttpMethod.POST);

      session.setAttribute(OAuth.OAUTH_ACCESS_TOKEN, JwtUtils.parse(resp.getAccessToken(), clientSecret, Constants.OAUTH2_JWT_CLAIMS));
    } catch (Exception e) {
      throw new OAuth2AuthenticationException(e);
    }
  }

  private void refreshToken(ServletRequest request, String refreshToken) {
    HttpServletRequest httpRequest = WebUtils.toHttp(request);

    Subject subject = SecurityUtils.getSubject();
    Session session = subject.getSession(false);

    String accessTokenUrl = UriComponentsBuilder.newInstance()
        .scheme(serverScheme)
        .host(serverHost)
        .port(serverPort)
        .path(serverContextPath + Constants.SERVER_ACCESS_TOKEN_PATH)
        .build().toUriString();

    String redirectUrl = httpRequest.getRequestURL().toString();
    if (StringUtils.isNotBlank(httpRequest.getQueryString())) {
      redirectUrl += "?" + httpRequest.getQueryString();
    }

    try {
      OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
      OAuthClientRequest req = OAuthClientRequest
          .tokenLocation(accessTokenUrl)
          .setGrantType(GrantType.REFRESH_TOKEN)
          .setClientId(clientId)
          .setClientSecret(clientSecret)
          .setRefreshToken(refreshToken)
          .setRedirectURI(redirectUrl)
          .buildQueryMessage();

      OAuthAccessTokenResponse resp = oAuthClient.accessToken(req, OAuth.HttpMethod.POST);
      session.setAttribute(OAuth.OAUTH_ACCESS_TOKEN, JwtUtils.parse(resp.getAccessToken(), clientSecret, Constants.OAUTH2_JWT_CLAIMS));
    } catch (Exception e) {
      throw new OAuth2AuthenticationException(e);
    }
  }

  public void setServerScheme(String serverScheme) {
    this.serverScheme = serverScheme;
  }

  public void setServerHost(String serverHost) {
    this.serverHost = serverHost;
  }

  public void setServerPort(int serverPort) {
    this.serverPort = serverPort;
  }

  public void setServerContextPath(String serverContextPath) {
    this.serverContextPath = serverContextPath;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }
}