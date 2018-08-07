package com.github.xfslove.autoconfigure.shiro.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hanwen on 02/01/2018.
 */
public class Jwt implements Serializable {

  private String subject;

  private Date expires;

  private Map<String, Object> claims = new HashMap<>();

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public Date getExpires() {
    return expires;
  }

  public void setExpires(Date expires) {
    this.expires = expires;
  }

  public Map<String, Object> getClaims() {
    return claims;
  }

  public void setClaims(Map<String, Object> claims) {
    this.claims = claims;
  }
}
