package com.github.xfslove.autoconfigure.shiro.endpoint;

import com.github.xfslove.autoconfigure.shiro.jwt.JwtUtils;
import com.github.xfslove.autoconfigure.shiro.model.*;
import com.github.xfslove.autoconfigure.shiro.service.*;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.issuer.UUIDValueGenerator;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * Created by hanwen on 2017/9/19.
 */
@RequestMapping(Constants.SERVER_ACCESS_TOKEN_PATH)
public class UaaAccessTokenEndpoint {

  private static final Logger LOGGER = LoggerFactory.getLogger(UaaAccessTokenEndpoint.class);

  private AccessClientService accessClientService;

  private AuthCodeService authCodeService;

  private AccessTokenService accessTokenService;

  private RoleService roleService;

  private AccountService accountService;

  private Long accessTokenExpires;

  private Long refreshTokenExpires;

  @RequestMapping(value = "", method = RequestMethod.POST)
  public ResponseEntity accessToken(
      HttpServletRequest request
  ) throws OAuthSystemException {

    try {
      OAuthTokenRequest tokenRequest = new OAuthTokenRequest(request);
      String grantType = tokenRequest.getGrantType();
      String clientId = tokenRequest.getClientId();
      String clientSecret = tokenRequest.getClientSecret();

      AccessClient accessClient = accessClientService.getByClient(clientId);
      if (accessClient == null) {
        throw OAuthProblemException.error(OAuthError.TokenResponse.INVALID_CLIENT, "client invalid");
      }
      if (!accessClient.isEnabled()) {
        throw OAuthProblemException.error(OAuthError.TokenResponse.UNAUTHORIZED_CLIENT, "client disabled");
      }
      if (!accessClient.getClientSecret().equals(clientSecret)) {
        throw OAuthProblemException.error(OAuthError.TokenResponse.UNAUTHORIZED_CLIENT, "client_secret invalid");
      }

      AccessToken accessToken = new AccessToken();
      OAuthIssuer oAuthIssuer = new OAuthIssuerImpl(new UUIDValueGenerator());
      accessToken.setAccessToken(oAuthIssuer.accessToken());
      accessToken.setRefreshToken(oAuthIssuer.refreshToken());
      accessToken.setAccessTokenExpires(new Date(System.currentTimeMillis() + 1000L * accessTokenExpires));
      accessToken.setRefreshTokenExpires(new Date(System.currentTimeMillis() + 1000L * refreshTokenExpires));

      String code = tokenRequest.getCode();
      if (GrantType.AUTHORIZATION_CODE.toString().equals(grantType)) {

        AuthCode authCode = authCodeService.getByCode(code);
        if (authCode == null) {
          throw OAuthProblemException.error(OAuthError.TokenResponse.INVALID_GRANT, "code invalid");
        }
        if (System.currentTimeMillis() > authCode.getExpires().getTime()) {
          throw OAuthProblemException.error(OAuthError.TokenResponse.INVALID_REQUEST, "code expires");
        }
        if (!authCode.getClientId().equals(accessClient.getClientId())) {
          throw OAuthProblemException.error(OAuthError.TokenResponse.INVALID_GRANT, "not client issued code");
        }

        authCodeService.remove(authCode.getCode());

        AccessToken issued = accessTokenService.getBySession(authCode.getSessionId(), authCode.getClientId());
        if (issued != null) {
          accessTokenService.remove(issued.getAccessToken());
        }

        accessToken.setUsername(authCode.getUsername());
        accessToken.setSessionId(authCode.getSessionId());

        accessToken.setClientId(authCode.getClientId());
        accessToken.setClientSecret(authCode.getClientSecret());
      } else if (GrantType.REFRESH_TOKEN.toString().equals(grantType)) {

        String refreshToken = tokenRequest.getRefreshToken();
        AccessToken expiredToken = accessTokenService.getByRefreshToken(refreshToken);
        if (expiredToken == null) {
          throw OAuthProblemException.error(OAuthError.TokenResponse.INVALID_REQUEST, "refresh_token invalid");
        }
        if (System.currentTimeMillis() > expiredToken.getRefreshTokenExpires().getTime()) {
          throw OAuthProblemException.error(OAuthError.TokenResponse.INVALID_REQUEST, "refresh_token expires");
        }

        accessTokenService.remove(expiredToken.getAccessToken());

        accessToken.setUsername(expiredToken.getUsername());
        accessToken.setSessionId(expiredToken.getSessionId());

        accessToken.setClientId(expiredToken.getClientId());
        accessToken.setClientSecret(expiredToken.getClientSecret());
      } else {
        throw OAuthProblemException.error(OAuthError.TokenResponse.UNSUPPORTED_GRANT_TYPE, "grant_type invalid");
      }

      accessTokenService.save(accessToken);

      OAuthResponse response = OAuthASResponse
          .tokenResponse(HttpServletResponse.SC_OK)
          .setAccessToken(JwtUtils.generate(createJwt(accessToken), accessToken.getClientSecret()))
          .buildJSONMessage();

      LOGGER.info("UAA SERVER INFO : {} get access_token[{}] success from server, issued client_id:[{}]", accessToken.getUsername(), accessToken.getAccessToken(), clientId);
      return new ResponseEntity<>(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
    } catch (OAuthProblemException ex) {

      OAuthResponse r = OAuthResponse.errorResponse(HttpServletResponse.SC_UNAUTHORIZED).error(ex).buildJSONMessage();
      return new ResponseEntity<>(r.getBody(), HttpStatus.valueOf(r.getResponseStatus()));
    }
  }

  private Jwt createJwt(AccessToken accessToken) {
    Jwt jwt = new Jwt();
    jwt.setSubject(accessToken.getUsername());
    jwt.setExpires(accessToken.getAccessTokenExpires());

    Account account = accountService.getByUsername(accessToken.getUsername());

    Map<String, Object> claims = jwt.getClaims();
    claims.put(Constants.ACCESS_TOKEN, accessToken.getAccessToken());
    claims.put(Constants.REFRESH_TOKEN, accessToken.getRefreshToken());
    claims.put(Constants.PERM_CODE, roleService.getPermCodes(accessToken.getClientId(), account.getUsername()));

    return jwt;
  }

  public void setAccessClientService(AccessClientService accessClientService) {
    this.accessClientService = accessClientService;
  }

  public void setAuthCodeService(AuthCodeService authCodeService) {
    this.authCodeService = authCodeService;
  }

  public void setAccessTokenService(AccessTokenService accessTokenService) {
    this.accessTokenService = accessTokenService;
  }

  public void setRoleService(RoleService roleService) {
    this.roleService = roleService;
  }

  public void setAccountService(AccountService accountService) {
    this.accountService = accountService;
  }

  public void setAccessTokenExpires(Long accessTokenExpires) {
    this.accessTokenExpires = accessTokenExpires;
  }

  public void setRefreshTokenExpires(Long refreshTokenExpires) {
    this.refreshTokenExpires = refreshTokenExpires;
  }
}
