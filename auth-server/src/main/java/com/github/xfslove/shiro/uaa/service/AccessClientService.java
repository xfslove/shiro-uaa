package com.github.xfslove.shiro.uaa.service;

import com.github.xfslove.shiro.uaa.model.AccessClient;

/**
 * Created by hanwen on 2017/10/19.
 */
public interface AccessClientService {

  AccessClient getById(Long id);

  AccessClient getByClient(String clientId);
}
