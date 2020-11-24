package com.github.xfslove.shiro.uaa.aop;

import org.apache.shiro.aop.AnnotationHandler;

/**
 * Created by hanwen on 2020/11/24.
 */
public class ShiroBizLoggerAnnotationHandler extends AnnotationHandler {

  public ShiroBizLoggerAnnotationHandler() {
    super(ShiroBizLogger.class);
  }
}
