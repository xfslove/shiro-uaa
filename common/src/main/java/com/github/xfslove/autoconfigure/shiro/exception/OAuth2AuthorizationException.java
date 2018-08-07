package com.github.xfslove.autoconfigure.shiro.exception;

import org.apache.shiro.authz.AuthorizationException;

/**
 * Created by hanwen on 2017/9/19.
 */
public class OAuth2AuthorizationException extends AuthorizationException {

  public OAuth2AuthorizationException() {
    super();
  }

  public OAuth2AuthorizationException(String message) {
    super(message);
  }

  public OAuth2AuthorizationException(Throwable cause) {
    super(cause);
  }

  public OAuth2AuthorizationException(String message, Throwable cause) {
    super(message, cause);
  }
}
