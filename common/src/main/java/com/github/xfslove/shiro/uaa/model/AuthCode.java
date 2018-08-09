package com.github.xfslove.shiro.uaa.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by hanwen on 2017/10/19.
 */
public class AuthCode implements Serializable {

  private Long id;

  private String code;

  private Long accountId;

  private Long accessClientId;

  private String sessionId;

  private Date expires;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Long getAccessClientId() {
    return accessClientId;
  }

  public void setAccessClientId(Long accessClientId) {
    this.accessClientId = accessClientId;
  }

  public Long getAccountId() {
    return accountId;
  }

  public void setAccountId(Long accountId) {
    this.accountId = accountId;
  }

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public Date getExpires() {
    return expires;
  }

  public void setExpires(Date expires) {
    this.expires = expires;
  }
}
