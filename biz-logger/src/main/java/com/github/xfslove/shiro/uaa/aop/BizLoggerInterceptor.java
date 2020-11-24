package com.github.xfslove.shiro.uaa.aop;

import com.github.xfslove.shiro.uaa.logger.BizLoggerEntity;
import com.github.xfslove.shiro.uaa.logger.BizLoggerEntityStringifier;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.aop.AnnotationMethodInterceptor;
import org.apache.shiro.aop.AnnotationResolver;
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

  private final BizLoggerEntityStringifier bizLoggerEntityStringifier;

  public BizLoggerInterceptor(BizLoggerEntityStringifier bizLoggerEntityStringifier, AnnotationResolver resolver) {
    super(new BizLoggerAnnotationHandler(), resolver);
    this.bizLoggerEntityStringifier = bizLoggerEntityStringifier;
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

    List<BizLoggerEntity> loggerEntities = new ArrayList<>();

    if (bizLogger.className()) {
      loggerEntities.add(new BizLoggerEntity("class_name", invocation.getMethod().getDeclaringClass().getName()));
    }

    if (bizLogger.methodName()) {
      loggerEntities.add(new BizLoggerEntity("method_name", invocation.getMethod().getName()));
    }

    int indiesLength = bizLogger.argumentIndies().length;
    int argLength = invocation.getArguments().length;
    if (indiesLength > 0) {
      Arrays.sort(bizLogger.argumentIndies());
      if (bizLogger.argumentIndies()[indiesLength - 1] <= argLength) {
        for (int i = 0; i < indiesLength; i++) {
          int index = bizLogger.argumentIndies()[i];
          Object argument = invocation.getArguments()[index - 1];
          loggerEntities.add(new BizLoggerEntity("argument_" + argument.getClass().getName(), argument));
        }
      } else {
        LOGGER.warn("BIZ LOGGER INFO : can not logging arguments because of configured wrong argument indies.");
      }
    }

    Object ret = invocation.proceed();

    if (bizLogger.returnValue()) {
      loggerEntities.add(new BizLoggerEntity("return", ret));
    }

    String principal = (String) SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal();

    LOGGER.info("BIZ LOGGER INFO : {} access at {}, remark: {}.", principal, bizLoggerEntityStringifier.stringifier(loggerEntities), bizLogger.remark());
    return ret;
  }

}
