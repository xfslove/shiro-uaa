package com.github.xfslove.autoconfigure.shiro.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * Created by hanwen on 2017/9/19.
 */
public class OAuth2AuthenticationException extends AuthenticationException {

  public OAuth2AuthenticationException() {
    super();
  }

  public OAuth2AuthenticationException(String message) {
    super(message);
  }

  public OAuth2AuthenticationException(Throwable cause) {
    super(cause);
  }

  public OAuth2AuthenticationException(String message, Throwable cause) {
    super(message, cause);
  }
}
