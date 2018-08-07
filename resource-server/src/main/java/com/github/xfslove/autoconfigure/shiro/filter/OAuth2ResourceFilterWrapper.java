package com.github.xfslove.autoconfigure.shiro.filter;

import javax.servlet.Filter;

/**
 * Created by hanwen on 2017/10/10.
 */
public class OAuth2ResourceFilterWrapper implements CustomFilterWrapper {

  private String serverScheme;

  private String serverHost;

  private int serverPort;

  private String serverContextPath;

  private String clientId;

  private String clientSecret;

  @Override
  public Filter getFilter() {
    OAuth2ResourceFilter filter = new OAuth2ResourceFilter();
    filter.setServerScheme(serverScheme);
    filter.setServerHost(serverHost);
    filter.setServerPort(serverPort);
    filter.setServerContextPath(serverContextPath);
    filter.setClientId(clientId);
    filter.setClientSecret(clientSecret);
    return filter;
  }

  @Override
  public String getName() {
    return "oauth-resource";
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
