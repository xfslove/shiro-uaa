package com.github.xfslove.autoconfigure.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.AllSuccessfulStrategy;
import org.apache.shiro.realm.Realm;

/**
 * Created by hanwen on 2017/9/21.
 */
public class AllSupportSuccessfulStrategy extends AllSuccessfulStrategy {

  @Override
  public AuthenticationInfo beforeAttempt(Realm realm, AuthenticationToken token, AuthenticationInfo info) throws AuthenticationException {
    return info;
  }
}
