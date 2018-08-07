package com.github.xfslove.autoconfigure.shiro.realm;

import com.github.xfslove.autoconfigure.shiro.exception.OAuth2AuthenticationException;
import com.github.xfslove.autoconfigure.shiro.exception.OAuth2AuthorizationException;
import com.github.xfslove.autoconfigure.shiro.model.Constants;
import com.github.xfslove.autoconfigure.shiro.model.Jwt;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by hanwen on 2017/9/19.
 */
public class OAuth2ResourceRealm extends AuthorizingRealm {

  public OAuth2ResourceRealm() {
  }

  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

    try {
      OAuth2ResourceToken oAuth2ResourceToken = (OAuth2ResourceToken) token;
      Jwt jwt = oAuth2ResourceToken.getJwt();

      List<Object> principals = new ArrayList<>();
      principals.add(jwt.getSubject());
      principals.add(jwt);

      return new SimpleAuthenticationInfo(principals, jwt.getClaims().get(Constants.ACCESS_TOKEN), getName());
    } catch (Exception e) {
      throw new OAuth2AuthenticationException(e);
    }

  }

  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

    try {
      Jwt jwt = (Jwt) principals.asList().get(1);
      Set<String> permCodes = new HashSet<>((List) jwt.getClaims().get(Constants.PERM_CODE));

      SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
      info.setStringPermissions(permCodes);
      return info;
    } catch (Exception e) {
      throw new OAuth2AuthorizationException(e);
    }
  }

  @Override
  public Class getAuthenticationTokenClass() {
    return OAuth2ResourceToken.class;
  }

  @Override
  protected boolean isAuthenticationCachingEnabled(AuthenticationToken token, AuthenticationInfo info) {
    // 认证信息不缓存
    return false;
  }

  @Override
  public boolean isAuthorizationCachingEnabled() {
    // 授权信息不缓存
    return false;
  }

  @Override
  protected void doClearCache(PrincipalCollection principals) {
    // 不用默认的失效机制, 而是在设置缓存时给失效时间
  }
}
