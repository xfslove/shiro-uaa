package com.github.xfslove.autoconfigure.shiro;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by hanwen on 2017/9/20.
 */
@ConfigurationProperties(prefix = "shiro.uaa.resource")
public class OAuth2ResourceProperties {

  private String serverScheme = "http";

  private String serverHost;

  private int serverPort;

  private String serverContextPath = "";

  private String clientId;

  private String clientSecret;

  public String getServerScheme() {
    return serverScheme;
  }

  public void setServerScheme(String serverScheme) {
    this.serverScheme = serverScheme;
  }

  public String getServerHost() {
    return serverHost;
  }

  public void setServerHost(String serverHost) {
    this.serverHost = serverHost;
  }

  public int getServerPort() {
    return serverPort;
  }

  public void setServerPort(int serverPort) {
    this.serverPort = serverPort;
  }

  public String getServerContextPath() {
    return serverContextPath;
  }

  public void setServerContextPath(String serverContextPath) {
    this.serverContextPath = serverContextPath;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getClientSecret() {
    return clientSecret;
  }

  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }
}
