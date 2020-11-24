package com.github.xfslove.shiro.uaa.aop;

import com.github.xfslove.shiro.uaa.logger.BizLoggerEntityStringifier;
import org.apache.shiro.spring.aop.SpringAnnotationResolver;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;

/**
 * Created by hanwen on 2020/11/24.
 */
public class ShiroBizLoggerAdvisor extends StaticMethodMatcherPointcutAdvisor {

  public ShiroBizLoggerAdvisor(BizLoggerEntityStringifier bizLoggerEntityStringifier) {
    setAdvice(new ShiroBizLoggerInterceptor(bizLoggerEntityStringifier, new SpringAnnotationResolver()));
  }

  @Override
  public boolean matches(Method method, Class<?> targetClass) {
    ShiroBizLogger shiroBizLogger = AnnotationUtils.findAnnotation(method, ShiroBizLogger.class);
    return shiroBizLogger != null;
  }
}
