package com.github.xfslove.shiro.uaa.service;

import com.github.xfslove.shiro.uaa.model.Role;

/**
 * Created by hanwen on 2017/6/12.
 */
public interface RoleService {

  Role getById(Long id);

  Role getByName(String name);
}
