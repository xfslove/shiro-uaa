package com.github.xfslove.shiro.uaa.service;


import com.github.xfslove.shiro.uaa.model.AuthCode;

/**
 * Created by hanwen on 2017/10/19.
 */
public interface AuthCodeService {

  void save(AuthCode entity);

  AuthCode getByCode(String code);

  boolean remove(String code);
}
