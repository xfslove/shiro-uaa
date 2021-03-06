package com.github.xfslove.shiro.uaa.aop;

import com.github.xfslove.shiro.uaa.logger.BizLoggerEntity;
import com.github.xfslove.shiro.uaa.logger.BizLoggerEntityProcessor;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.aop.AnnotationMethodInterceptor;
import org.apache.shiro.aop.AnnotationResolver;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hanwen on 2020/11/24.
 */
public class BizLoggerInterceptor extends AnnotationMethodInterceptor implements org.aopalliance.intercept.MethodInterceptor {

  private static final Logger LOGGER = LoggerFactory.getLogger(BizLoggerInterceptor.class);

  private final BizLoggerEntityProcessor bizLoggerEntityProcessor;

  public BizLoggerInterceptor(BizLoggerEntityProcessor bizLoggerEntityProcessor, AnnotationResolver resolver) {
    super(new BizLoggerAnnotationHandler(), resolver);
    this.bizLoggerEntityProcessor = bizLoggerEntityProcessor;
  }

  @Override
  public Object invoke(org.aopalliance.intercept.MethodInvocation invocation) throws Throwable {
    return invoke(new org.apache.shiro.aop.MethodInvocation() {
      @Override
      public Object proceed() throws Throwable {
        return invocation.proceed();
      }

      @Override
      public Method getMethod() {
        return invocation.getMethod();
      }

      @Override
      public Object[] getArguments() {
        return invocation.getArguments();
      }

      @Override
      public Object getThis() {
        return invocation.getThis();
      }
    });
  }

  @Override
  public Object invoke(org.apache.shiro.aop.MethodInvocation invocation) throws Throwable {

    if (AopUtils.isAopProxy(invocation.getThis())) {
      return invocation.proceed();
    }

    Annotation annotation = getAnnotation(invocation);

    if (!(annotation instanceof BizLogger)) {
      return invocation.proceed();
    }

    BizLogger bizLogger = (BizLogger) annotation;

    List<BizLoggerEntity> loggerEntityList = new ArrayList<>();

    Subject subject = SecurityUtils.getSubject();
    if (bizLogger.subject() && subject.isAuthenticated()) {
      loggerEntityList.add(new BizLoggerEntity("subject", subject.getPrincipals().getPrimaryPrincipal()));
    }

    Method method = invocation.getMethod();
    if (bizLogger.class_()) {
      loggerEntityList.add(new BizLoggerEntity("class_", method.getDeclaringClass().getName()));
    }

    if (bizLogger.method()) {
      loggerEntityList.add(new BizLoggerEntity("method", method.getName()));
    }

    for (int i = 0; i < bizLogger.arguments().length; i++) {
      int index = bizLogger.arguments()[i];
      if (index > method.getParameterTypes().length) {
        LOGGER.warn("BIZ LOGGER INFO : can not logging arguments because of configured wrong argument indies[{}].", index);
      } else {
        Class<?> argumentClass = method.getParameterTypes()[index - 1];
        loggerEntityList.add(new BizLoggerEntity("argument_" + argumentClass.getName(), invocation.getArguments()[index - 1]));
      }
    }

    if (StringUtils.isNotBlank(bizLogger.remark())) {
      loggerEntityList.add(new BizLoggerEntity("remark", bizLogger.remark()));
    }

    try {
      Object ret = invocation.proceed();
      if (bizLogger.return_()) {
        loggerEntityList.add(new BizLoggerEntity("return_", ret));
      }
      return ret;
    } finally {

      bizLoggerEntityProcessor.process(loggerEntityList);
    }
  }

}
