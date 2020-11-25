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
   * @return if log subject name
   */
  boolean subject() default true;

  /**
   * @return if log class name
   */
  boolean class_() default true;

  /**
   * @return if log method name
   */
  boolean method() default true;

  /**
   * 1-base
   *
   * @return argument indies to log, but if has wrong indies will not log it.
   */
  int[] arguments() default {};

  /**
   * @return if log return value
   */
  boolean return_() default false;

  /**
   * @return logger remark
   */
  String remark() default "";
}
