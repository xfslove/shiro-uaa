package com.github.xfslove.shiro.uaa.logger;

import java.io.Serializable;

/**
 * {@link BizLoggerEntity#type}'s build-in values:
 * <li>subject</li>
 * <li>class_</li>
 * <li>method</li>
 * <li>argument_*(* present argument's class name)</li>
 * <li>return_</li>
 * <li>remark</li>
 * <p>
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
