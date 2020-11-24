package com.github.xfslove.shiro.uaa;

import com.github.xfslove.shiro.uaa.aop.ShiroBizLoggerAdvisor;
import com.github.xfslove.shiro.uaa.logger.DefaultBizLoggerEntityStringifier;
import com.github.xfslove.shiro.uaa.logger.BizLoggerEntityStringifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * Created by hanwen on 2020/11/24.
 */
public class ShiroBizLoggerAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(BizLoggerEntityStringifier.class)
  public BizLoggerEntityStringifier loggerEntityStringfier() {
    return new DefaultBizLoggerEntityStringifier();
  }

  @Bean
  public ShiroBizLoggerAdvisor shiroBizLoggerAdvisor(BizLoggerEntityStringifier bizLoggerEntityStringifier) {
    return new ShiroBizLoggerAdvisor(bizLoggerEntityStringifier);
  }
}