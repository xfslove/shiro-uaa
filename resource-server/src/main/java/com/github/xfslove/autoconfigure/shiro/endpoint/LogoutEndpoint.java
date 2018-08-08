package com.github.xfslove.autoconfigure.shiro.endpoint;

import com.github.xfslove.autoconfigure.shiro.model.Constants;
import com.github.xfslove.autoconfigure.shiro.utils.OAuthParamsClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by hanwen on 2017/10/30.
 */
@RequestMapping(value = "${shiro.logoutUrl}")
public class LogoutEndpoint {

  private static final Logger LOGGER = LoggerFactory.getLogger(LogoutEndpoint.class);

  private String serverScheme;

  private String serverHost;

  private int serverPort;

  private String serverContextPath;

  private String clientId;

  @RequestMapping(value = "", method = RequestMethod.GET)
  public void logout(
      HttpServletResponse response,
      @RequestParam(value = OAuth.OAUTH_REDIRECT_URI) String redirectUrl
  ) throws Exception {
    String notifyUrl = UriComponentsBuilder.newInstance()
        .scheme(serverScheme)
        .host(serverHost)
        .port(serverPort)
        .path(serverContextPath + Constants.SERVER_LOGOUT_PATH)
        .build().toUriString();

    // logout from auth-server
    OAuthClientRequest req = new OAuthParamsClientRequest(notifyUrl)
        .param(OAuth.OAUTH_CLIENT_ID, clientId)
        .param(OAuth.OAUTH_REDIRECT_URI, redirectUrl)
        .buildQueryMessage();

    Subject subject = SecurityUtils.getSubject();
    if (!subject.isAuthenticated()) {
      response.sendRedirect(req.getLocationUri());
      return;
    }

    String username = (String) subject.getPrincipals().getPrimaryPrincipal();
    subject.logout();
    LOGGER.info("UAA RESOURCE INFO : {} logout success, redirect to {}",username, redirectUrl);
    response.sendRedirect(req.getLocationUri());
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
}
