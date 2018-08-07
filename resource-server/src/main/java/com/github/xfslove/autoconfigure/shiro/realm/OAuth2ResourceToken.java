package com.github.xfslove.autoconfigure.shiro.realm;

import com.github.xfslove.autoconfigure.shiro.model.Constants;
import com.github.xfslove.autoconfigure.shiro.model.Jwt;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * Created by hanwen on 2017/9/19.
 */
public class OAuth2ResourceToken implements AuthenticationToken {

  private Jwt jwt;

  public OAuth2ResourceToken(Jwt jwt) {
    this.jwt = jwt;
  }

  @Override
  public Object getPrincipal() {
    return jwt.getSubject();
  }

  @Override
  public Object getCredentials() {
    return jwt.getClaims().get(Constants.ACCESS_TOKEN);
  }

  public Jwt getJwt() {
    return jwt;
  }
}
