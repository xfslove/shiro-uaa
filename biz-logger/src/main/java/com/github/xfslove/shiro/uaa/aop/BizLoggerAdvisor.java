package com.github.xfslove.shiro.uaa.aop;

import com.github.xfslove.shiro.uaa.logger.BizLoggerEntityProcessor;
import org.apache.shiro.spring.aop.SpringAnnotationResolver;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;

/**
 * Created by hanwen on 2020/11/24.
 */
public class BizLoggerAdvisor extends StaticMethodMatcherPointcutAdvisor {

  public BizLoggerAdvisor(BizLoggerEntityProcessor bizLoggerEntityProcessor) {
    setAdvice(new BizLoggerInterceptor(bizLoggerEntityProcessor, new SpringAnnotationResolver()));
  }

  @Override
  public boolean matches(Method method, Class<?> targetClass) {
    BizLogger bizLogger = AnnotationUtils.findAnnotation(method, BizLogger.class);
    return bizLogger != null;
  }
}
