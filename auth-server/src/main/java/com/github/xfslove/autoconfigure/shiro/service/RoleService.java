package com.github.xfslove.autoconfigure.shiro.service;

import java.util.Set;

/**
 * Created by hanwen on 2017/6/12.
 */
public interface RoleService {

  Set<String> getPermCodes(String clientId, Long accountId);
}
