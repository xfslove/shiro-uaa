package com.github.xfslove.autoconfigure.shiro;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by hanwen on 2017/9/20.
 */
@ConfigurationProperties(prefix = "shiro.uaa.server")
public class OAuth2ServerProperties {

  /**
   * seconds
   */
  private Long accessTokenExpires = 60L * 5;

  /**
   * seconds
   */
  private Long refreshTokenExpires = 60L * 30;

  /**
   * seconds
   */
  private Long codeExpires = 60L * 5;

  public Long getAccessTokenExpires() {
    return accessTokenExpires;
  }

  public void setAccessTokenExpires(Long accessTokenExpires) {
    this.accessTokenExpires = accessTokenExpires;
  }

  public Long getRefreshTokenExpires() {
    return refreshTokenExpires;
  }

  public void setRefreshTokenExpires(Long refreshTokenExpires) {
    this.refreshTokenExpires = refreshTokenExpires;
  }

  public void setCodeExpires(Long codeExpires) {
    this.codeExpires = codeExpires;
  }

  public Long getCodeExpires() {
    return codeExpires;
  }
}
