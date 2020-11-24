package com.github.xfslove.shiro.uaa.logger;

import java.io.Serializable;

/**
 * Created by hanwen on 2020/11/24.
 */
public class BizLoggerEntity implements Serializable {

  public String type;

  public Object value;

  public BizLoggerEntity(String type, Object value) {
    this.type = type;
    this.value = value;
  }

  public String getType() {
    return type;
  }

  public Object getValue() {
    return value;
  }
}
