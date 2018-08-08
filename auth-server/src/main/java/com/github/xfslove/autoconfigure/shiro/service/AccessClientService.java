package com.github.xfslove.autoconfigure.shiro.service;

import com.github.xfslove.autoconfigure.shiro.model.AccessClient;

/**
 * Created by hanwen on 2017/10/19.
 */
public interface AccessClientService {

  AccessClient getById(Long id);

  AccessClient getByClient(String clientId);
}
