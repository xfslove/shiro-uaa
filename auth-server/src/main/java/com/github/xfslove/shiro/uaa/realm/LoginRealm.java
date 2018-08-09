package com.github.xfslove.shiro.uaa.realm;

import com.github.xfslove.shiro.uaa.model.Account;
import com.github.xfslove.shiro.uaa.service.AccountService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hanwen on 2017/6/9.
 */
public class LoginRealm extends AuthenticatingRealm {

  private AccountService accountService;

  private CredentialsMatcher credentialsMatcher;

  public LoginRealm() {
  }

  @Override
  public boolean supports(AuthenticationToken token) {
    return UsernamePasswordToken.class.isAssignableFrom(token.getClass());
  }

  @Override
  public CredentialsMatcher getCredentialsMatcher() {
    return credentialsMatcher;
  }

  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
    UsernamePasswordToken loginToken = (UsernamePasswordToken) token;
    String username = loginToken.getUsername();
    Account account = accountService.getByUsername(username);
    if (account == null) {
      throw new UnknownAccountException(username + " not valid");
    }
    if (!account.isEnabled()) {
      throw new LockedAccountException(username + " disabled");
    }

    List<Object> principals = new ArrayList<>();
    principals.add(account.getUsername());
    principals.add(account);

    return new SimpleAuthenticationInfo(principals, account.getPassword(), getName());
  }

  @Override
  protected boolean isAuthenticationCachingEnabled(AuthenticationToken token, AuthenticationInfo info) {
    // 认证信息不缓存
    return false;
  }

  @Override
  protected void doClearCache(PrincipalCollection principals) {
    // 不用默认的失效机制, 而是在设置缓存时给失效时间
  }

  public void setAccountService(AccountService accountService) {
    this.accountService = accountService;
  }

  @Override
  public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
    this.credentialsMatcher = credentialsMatcher;
  }
}
