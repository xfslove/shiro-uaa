package com.github.xfslove.autoconfigure.shiro.filter;

import com.github.xfslove.autoconfigure.shiro.service.AccessTokenService;

import javax.servlet.Filter;

/**
 * Created by hanwen on 2017/10/10.
 */
public class AuthServerFilterWrapper implements CustomFilterWrapper {

  private AccessTokenService accessTokenService;

  @Override
  public Filter getFilter() {
//    AuthServerFilter oAuth2ServerFilter = new AuthServerFilter();
////    oAuth2ServerFilter.setAccessTokenService(accessTokenService);
////    return oAuth2ServerFilter;
    return null;
  }

  @Override
  public String getName() {
    return "oauth-server";
  }

  public void setAccessTokenService(AccessTokenService accessTokenService) {
    this.accessTokenService = accessTokenService;
  }

}
