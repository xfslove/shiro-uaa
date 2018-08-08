package com.github.xfslove.autoconfigure.shiro.service;

import com.github.xfslove.autoconfigure.shiro.model.AccessToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by hanwen on 2018/8/8.
 */
public class MemoryAccessTokenService implements AccessTokenService {

  private Map<String, AccessToken> db = new ConcurrentHashMap<>(64);

  @Override
  public void save(AccessToken accessToken) {
    db.put(accessToken.getAccessToken(),accessToken);
  }

  @Override
  public AccessToken getByRefreshToken(String refreshToken) {
    for (AccessToken value : db.values()) {
      if (value.getRefreshToken().equals(refreshToken)) {
        return value;
      }
    }
    return null;
  }

  @Override
  public AccessToken getBySession(String sessionId, Long accessClientId) {
    for (AccessToken value : db.values()) {
      if (value.getSessionId().equals(sessionId) && value.getAccessClientId().equals(accessClientId)) {
        return value;
      }
    }
    return null;
  }

  @Override
  public List<AccessToken> getBySession(String sessionId) {
    List<AccessToken> result = new ArrayList<>();
    for (AccessToken value : db.values()) {
      if (value.getSessionId().equals(sessionId)) {
        result.add(value);
      }
    }
    return result;
  }

  @Override
  public boolean remove(String accessToken) {
    return db.remove(accessToken) != null;
  }
}
