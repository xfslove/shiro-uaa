package com.github.xfslove.shiro.uaa.endpoint;

import com.github.xfslove.shiro.uaa.model.AccessClient;
import com.github.xfslove.shiro.uaa.model.Account;
import com.github.xfslove.shiro.uaa.model.AuthCode;
import com.github.xfslove.shiro.uaa.model.Constants;
import com.github.xfslove.shiro.uaa.service.AccessClientService;
import com.github.xfslove.shiro.uaa.service.AuthCodeService;
import org.apache.commons.lang3.StringUtils;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.issuer.UUIDValueGenerator;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Created by hanwen on 2017/9/19.
 */
@RequestMapping(Constants.SERVER_AUTHENTICATION_PATH)
public class UaaAuthenticationEndpoint {

  private static final Logger LOGGER = LoggerFactory.getLogger(UaaAuthenticationEndpoint.class);

  private AccessClientService accessClientService;

  private AuthCodeService authCodeService;

  private String loginUrl;

  private Long codeExpires;

  private String forwardErrorUrl;

  @RequestMapping(value = "", method = RequestMethod.GET)
  public ModelAndView index(
      ModelAndView modelAndView,
      HttpServletRequest request,
      HttpServletResponse response,
      @RequestParam(OAuth.OAUTH_REDIRECT_URI) String redirectURL
  ) throws IOException, OAuthSystemException {

    try {
      OAuthAuthzRequest authzRequest = new OAuthAuthzRequest(request);
      String responseType = authzRequest.getResponseType();
      String clientId = authzRequest.getClientId();

      AccessClient accessClient = accessClientService.getByClient(clientId);

      if (accessClient == null) {
        throw OAuthProblemException.error(OAuthError.CodeResponse.UNAUTHORIZED_CLIENT, "client invalid");
      }
      if (!accessClient.isEnabled()) {
        throw OAuthProblemException.error(OAuthError.TokenResponse.UNAUTHORIZED_CLIENT, "client disabled");
      }
      if (!OAuth.OAUTH_CODE.equals(responseType)) {
        throw OAuthProblemException.error(OAuthError.CodeResponse.UNSUPPORTED_RESPONSE_TYPE, "response_type invalid");
      }

      // sso
      Subject subject = SecurityUtils.getSubject();
      if (subject.isAuthenticated()) {
        modelAndView.setViewName("forward:" + Constants.SERVER_AUTHENTICATION_PATH + "/approve");
        return modelAndView;
      }

      modelAndView.setViewName("forward:" + loginUrl);
      return modelAndView;
    } catch (OAuthProblemException e) {

      if (StringUtils.isBlank(forwardErrorUrl)) {

        OAuthResponse resp = OAuthASResponse
            .errorResponse(HttpServletResponse.SC_FOUND)
            .error(e)
            .location(redirectURL)
            .buildQueryMessage();
        response.sendRedirect(resp.getLocationUri());
        return null;
      } else {
        modelAndView.setViewName("forward:" + forwardErrorUrl);
        return modelAndView;
      }
    }
  }

  @RequestMapping(value = "/approve", method = RequestMethod.GET)
  public ModelAndView approve(
      ModelAndView modelAndView,
      HttpServletRequest request,
      HttpServletResponse response,
      @RequestParam(OAuth.OAUTH_REDIRECT_URI) String redirectURL
  ) throws OAuthSystemException, IOException {

    try {
      OAuthAuthzRequest authzRequest = new OAuthAuthzRequest(request);
      String responseType = authzRequest.getResponseType();
      String clientId = authzRequest.getClientId();

      Subject subject = SecurityUtils.getSubject();

      if (!subject.isAuthenticated()) {
        throw OAuthProblemException.error(OAuthError.CodeResponse.ACCESS_DENIED, "subject not login");
      }
      AccessClient accessClient = accessClientService.getByClient(clientId);

      if (accessClient == null) {
        throw OAuthProblemException.error(OAuthError.CodeResponse.UNAUTHORIZED_CLIENT, "client invalid");
      }
      if (!accessClient.isEnabled()) {
        throw OAuthProblemException.error(OAuthError.TokenResponse.UNAUTHORIZED_CLIENT, "client disabled");
      }
      if (!OAuth.OAUTH_CODE.equals(responseType)) {
        throw OAuthProblemException.error(OAuthError.CodeResponse.UNSUPPORTED_RESPONSE_TYPE, "response_type invalid");
      }

      AuthCode authCode = new AuthCode();
      authCode.setSessionId((String) subject.getSession(false).getId());
      authCode.setCode(new OAuthIssuerImpl(new UUIDValueGenerator()).authorizationCode());
      Account account = (Account) subject.getPrincipals().asList().get(1);
      authCode.setAccountId(account.getId());
      authCode.setExpires(new Date(System.currentTimeMillis() + 1000L * codeExpires));
      authCode.setAccessClientId(accessClient.getId());
      authCodeService.save(authCode);

      OAuthResponse resp = OAuthASResponse
          .authorizationResponse(request, HttpServletResponse.SC_FOUND)
          .setCode(authCode.getCode())
          .location(redirectURL)
          .buildQueryMessage();
      String redirectUrl = resp.getLocationUri();

      LOGGER.info("UAA SERVER INFO : {} get auth_code success from server, issued client_id:[{}], redirect to {}", account.getUsername(), accessClient.getClientId(), redirectUrl);
      response.sendRedirect(redirectUrl);
      return null;
    } catch (OAuthProblemException ex) {

      if (StringUtils.isBlank(forwardErrorUrl)) {

        OAuthResponse resp = OAuthASResponse
            .errorResponse(HttpServletResponse.SC_FOUND)
            .error(ex)
            .location(redirectURL)
            .buildQueryMessage();
        response.sendRedirect(resp.getLocationUri());
        return null;
      } else {
        modelAndView.setViewName("forward:" + forwardErrorUrl);
        return modelAndView;
      }
    }
  }

  public void setAccessClientService(AccessClientService accessClientService) {
    this.accessClientService = accessClientService;
  }

  public void setAuthCodeService(AuthCodeService authCodeService) {
    this.authCodeService = authCodeService;
  }

  public void setLoginUrl(String loginUrl) {
    this.loginUrl = loginUrl;
  }

  public void setCodeExpires(Long codeExpires) {
    this.codeExpires = codeExpires;
  }

  public void setForwardErrorUrl(String forwardErrorUrl) {
    this.forwardErrorUrl = forwardErrorUrl;
  }
}
