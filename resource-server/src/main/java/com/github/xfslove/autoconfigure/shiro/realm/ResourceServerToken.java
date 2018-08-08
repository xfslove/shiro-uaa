package com.github.xfslove.autoconfigure.shiro.realm;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * Created by hanwen on 2017/9/19.
 */
public class ResourceServerToken implements AuthenticationToken {

  private String accessToken;

  private String credentials;

  public ResourceServerToken(String accessToken, String credentials) {
    this.accessToken = accessToken;
    this.credentials = credentials;
  }

  @Override
  public Object getPrincipal() {
    return accessToken;
  }

  @Override
  public Object getCredentials() {
    return credentials;
  }
}
