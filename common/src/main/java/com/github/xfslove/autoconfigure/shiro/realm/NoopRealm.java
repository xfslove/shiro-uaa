package com.github.xfslove.autoconfigure.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.realm.Realm;

/**
 * Created by hanwen on 2018/4/19.
 */
public class NoopRealm implements Realm {

  @Override
  public String getName() {
    return "noop";
  }

  @Override
  public boolean supports(AuthenticationToken token) {
    return false;
  }

  @Override
  public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
    return null;
  }
}
