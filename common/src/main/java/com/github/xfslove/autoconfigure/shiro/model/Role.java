package com.github.xfslove.autoconfigure.shiro.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by hanwen on 2017/6/12.
 */
public class Role implements Serializable {

  private Long id;

  /**
   * 角色名，和shiro的Role相关
   */
  private String name;

  private boolean enabled = true;

  /**
   * 角色的授权码, 和shiro的stringPermission相关
   */
  private Set<String> permCodes = new HashSet<>();

  /**
   * 所属接入access cli
   */
  private Long accessClientId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public Set<String> getPermCodes() {
    return permCodes;
  }

  public void setPermCodes(Set<String> permCodes) {
    this.permCodes = permCodes;
  }

  public Long getAccessClientId() {
    return accessClientId;
  }

  public void setAccessClientId(Long accessClientId) {
    this.accessClientId = accessClientId;
  }
}
