package com.github.xfslove.autoconfigure.shiro.service;

import com.github.xfslove.autoconfigure.shiro.model.Role;

/**
 * Created by hanwen on 2017/6/12.
 */
public interface RoleService {

  Role getById(Long id);

  Role getByName(String name);
}
