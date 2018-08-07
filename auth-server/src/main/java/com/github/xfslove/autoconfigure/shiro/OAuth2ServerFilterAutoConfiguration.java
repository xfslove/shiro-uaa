package com.github.xfslove.autoconfigure.shiro;

import com.github.xfslove.autoconfigure.shiro.filter.CustomFilterChainDefinition;
import com.github.xfslove.autoconfigure.shiro.filter.CustomFilterWrapper;
import com.github.xfslove.autoconfigure.shiro.filter.OAuth2ServerFilterChainDefinition;
import com.github.xfslove.autoconfigure.shiro.filter.OAuth2ServerFilterWrapper;
import com.github.xfslove.autoconfigure.shiro.service.AccessTokenService;
import org.apache.shiro.realm.Realm;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * Created by hanwen on 2017/9/21.
 */
@AutoConfigureBefore({ShiroFilterAutoConfiguration.class, ShiroAutoConfiguration.class})
@ConditionalOnClass(Realm.class)
public class OAuth2ServerFilterAutoConfiguration {

  @Bean
  protected CustomFilterWrapper oauth2ServerFilter(ApplicationContext applicationContext) {
    OAuth2ServerFilterWrapper oAuth2ServerFilterWrapper = new OAuth2ServerFilterWrapper();
    oAuth2ServerFilterWrapper.setAccessTokenService(applicationContext.getBean(AccessTokenService.class));
    return oAuth2ServerFilterWrapper;
  }

  @Bean
  public CustomFilterChainDefinition oauth2ServerFilterChain() {
    return new OAuth2ServerFilterChainDefinition();
  }
}
