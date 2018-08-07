package com.github.xfslove.autoconfigure.shiro.endpoint;

import com.github.xfslove.autoconfigure.shiro.model.AccessClient;
import com.github.xfslove.autoconfigure.shiro.model.Constants;
import com.github.xfslove.autoconfigure.shiro.model.AccessToken;
import com.github.xfslove.autoconfigure.shiro.service.AccessClientService;
import com.github.xfslove.autoconfigure.shiro.service.AccessTokenService;
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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by hanwen on 2017/10/19.
 */
@RequestMapping(Constants.SERVER_LOGOUT_PATH)
public class UaaLogoutEndpoint {

  private static final Logger LOGGER = LoggerFactory.getLogger(UaaLogoutEndpoint.class);

  private AccessClientService accessClientService;

  private AccessTokenService accessTokenService;

  @RequestMapping(value = "", method = RequestMethod.GET)
  public void logout(
      @RequestParam(OAuth.OAUTH_CLIENT_ID) String clientId,
      @RequestParam(OAuth.OAUTH_REDIRECT_URI) String redirectURL,
      HttpServletResponse response
  ) throws OAuthSystemException, IOException {

    try {
      AccessClient accessClient = accessClientService.getByClient(clientId);
      if (accessClient == null) {
        throw OAuthProblemException.error(OAuthError.TokenResponse.UNAUTHORIZED_CLIENT, "client invalid");
      }
      if (!accessClient.isEnabled()) {
        throw OAuthProblemException.error(OAuthError.TokenResponse.UNAUTHORIZED_CLIENT, "client disabled");
      }

      Subject subject = SecurityUtils.getSubject();
      if (!subject.isAuthenticated()) {
        response.sendRedirect(redirectURL);
        return;
      }

      List<AccessToken> tokens = accessTokenService.getBySession((String) subject.getSession(false).getId());
      for (AccessToken token : tokens) {
        accessTokenService.remove(token.getAccessToken());
      }

      String username = (String) subject.getPrincipals().getPrimaryPrincipal();
      subject.logout();
      LOGGER.info("UAA SERVER INFO : {} logout success from server, issued client_id:[{}]", username, clientId);

      response.sendRedirect(redirectURL);
    } catch (OAuthProblemException ex) {

      OAuthResponse resp = OAuthASResponse
          .errorResponse(HttpServletResponse.SC_FOUND)
          .error(ex)
          .location(redirectURL)
          .buildQueryMessage();
      response.sendRedirect(resp.getLocationUri());
    }
  }

  public void setAccessClientService(AccessClientService accessClientService) {
    this.accessClientService = accessClientService;
  }

  public void setAccessTokenService(AccessTokenService accessTokenService) {
    this.accessTokenService = accessTokenService;
  }
}
