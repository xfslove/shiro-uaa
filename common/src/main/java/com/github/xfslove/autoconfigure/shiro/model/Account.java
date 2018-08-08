package com.github.xfslove.autoconfigure.shiro.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by hanwen on 2017/6/9.
 */
public class Account implements Serializable {

  private Long id;

  private String username;

  private String password;

  private boolean enabled = true;

  private Set<Long> roleIds = new HashSet<>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public Set<Long> getRoleIds() {
    return roleIds;
  }

  public void setRoleIds(Set<Long> roleIds) {
    this.roleIds = roleIds;
  }
}
