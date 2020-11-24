package com.github.xfslove.shiro.uaa.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by hanwen on 2020/11/24.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ShiroBizLogger {

  boolean className() default true;

  boolean methodName() default true;

  int[] argumentIndies() default {};

  boolean returnValue() default false;
}
