package com.github.xfslove.shiro.uaa.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by hanwen on 2020/11/24.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BizLogger {

  /**
   * @return logger remark
   */
  String remark() default "";

  /**
   * @return if log class name
   */
  boolean className() default true;

  /**
   * @return if log method name
   */
  boolean methodName() default true;

  /**
   * @return argument indies to log, but if has wrong indies will not log it.
   */
  int[] argumentIndies() default {};

  /**
   * @return if log return value
   */
  boolean returnValue() default false;
}
