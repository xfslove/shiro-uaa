package com.github.xfslove.autoconfigure.shiro.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by hanwen on 2017/10/19.
 */
public class AccessToken implements Serializable {

  private Long id;

  private String accessToken;

  private String refreshToken;

  private Date accessTokenExpires;

  private Date refreshTokenExpires;

  private String username;

  private String clientId;

  private String clientSecret;

  private String sessionId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public Date getAccessTokenExpires() {
    return accessTokenExpires;
  }

  public void setAccessTokenExpires(Date accessTokenExpires) {
    this.accessTokenExpires = accessTokenExpires;
  }

  public Date getRefreshTokenExpires() {
    return refreshTokenExpires;
  }

  public void setRefreshTokenExpires(Date refreshTokenExpires) {
    this.refreshTokenExpires = refreshTokenExpires;
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

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }
}
