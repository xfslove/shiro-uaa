package com.github.xfslove.autoconfigure.shiro.service;


import com.github.xfslove.autoconfigure.shiro.model.AccessToken;

import java.util.List;

/**
 * Created by hanwen on 2017/10/19.
 */
public interface AccessTokenService {

  void save(AccessToken entity);

  AccessToken getByAccessToken(String accessToken);

  AccessToken getByRefreshToken(String refreshToken);

  AccessToken getBySession(String sessionId, String clientId);

  List<AccessToken> getBySession(String sessionId);

  boolean remove(String accessToken);
}
