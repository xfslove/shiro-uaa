package com.github.xfslove.shiro.uaa.realm;

import com.github.xfslove.shiro.uaa.jwt.JwtUtils;
import com.github.xfslove.shiro.uaa.model.Constants;
import com.github.xfslove.shiro.uaa.model.Jwt;
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

/**
 * Created by hanwen on 2017/9/19.
 */
public class ResourceServerRealm extends AuthorizingRealm {

  public ResourceServerRealm() {
  }

  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
    ResourceServerToken resourceServerToken = (ResourceServerToken) token;
    String accessToken = (String) resourceServerToken.getPrincipal();
    // jwt valid
    Jwt jwt = JwtUtils.parse(accessToken, (String) resourceServerToken.getCredentials(), Constants.OAUTH2_JWT_CLAIMS);

    List<Object> principals = new ArrayList<>();
    principals.add(jwt.getSubject());
    principals.add(jwt);

    return new SimpleAuthenticationInfo(principals, resourceServerToken.getCredentials(), getName());
  }

  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    Jwt jwt = (Jwt) principals.asList().get(1);

    SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
    info.setRoles(new HashSet<String>((List) jwt.getClaims().get(Constants.PERM_ROLE)));
    info.setStringPermissions(new HashSet<String>((List) jwt.getClaims().get(Constants.PERM_CODE)));
    return info;
  }

  @Override
  public Class getAuthenticationTokenClass() {
    return ResourceServerToken.class;
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
