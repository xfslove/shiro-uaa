package com.github.xfslove.autoconfigure.shiro.service;

import com.github.xfslove.autoconfigure.shiro.model.Account;

import java.util.Set;

/**
 * Created by hanwen on 2017/6/8.
 */
public interface AccountService {

  Account getById(Long id);

  Account getByUsername(String username);

  Set<String> getPermCodes(Long id, Long accessClientId);

  Set<String> getPermRoles(Long id, Long accessClientId);
}
