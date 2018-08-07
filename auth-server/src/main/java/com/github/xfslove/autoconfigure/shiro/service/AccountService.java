package com.github.xfslove.autoconfigure.shiro.service;

import com.github.xfslove.autoconfigure.shiro.model.Account;

/**
 * Created by hanwen on 2017/6/8.
 */
public interface AccountService {

  Account getByUsername(String username);
}
