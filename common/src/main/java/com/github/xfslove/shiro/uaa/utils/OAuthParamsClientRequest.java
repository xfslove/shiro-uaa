package com.github.xfslove.shiro.uaa.utils;

import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;

/**
 * Created by hanwen on 2017/6/8.
 */
public class OAuthParamsClientRequest extends OAuthBearerClientRequest {

  public OAuthParamsClientRequest(String url) {
    super(url);
  }

  public OAuthParamsClientRequest param(String name, String value) {
    this.parameters.put(name, value);
    return this;
  }

}
