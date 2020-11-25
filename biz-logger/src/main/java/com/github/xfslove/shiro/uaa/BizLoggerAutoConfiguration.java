package com.github.xfslove.shiro.uaa;

import com.github.xfslove.shiro.uaa.aop.BizLoggerAdvisor;
import com.github.xfslove.shiro.uaa.logger.BizLoggerEntityProcessor;
import com.github.xfslove.shiro.uaa.logger.DefaultSlf4jProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * Created by hanwen on 2020/11/24.
 */
public class BizLoggerAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(BizLoggerEntityProcessor.class)
  public BizLoggerEntityProcessor bizLoggerEntityProcessor() {
    return new DefaultSlf4jProcessor();
  }

  @Bean
  public BizLoggerAdvisor shiroBizLoggerAdvisor(BizLoggerEntityProcessor bizLoggerEntityProcessor) {
    return new BizLoggerAdvisor(bizLoggerEntityProcessor);
  }
}
