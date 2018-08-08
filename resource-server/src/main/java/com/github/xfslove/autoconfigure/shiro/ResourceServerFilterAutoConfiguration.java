package com.github.xfslove.autoconfigure.shiro;

import com.github.xfslove.autoconfigure.shiro.filter.CustomFilterChainDefinition;
import com.github.xfslove.autoconfigure.shiro.filter.CustomFilterWrapper;
import com.github.xfslove.autoconfigure.shiro.filter.ResourceServerFilterChainDefinition;
import com.github.xfslove.autoconfigure.shiro.filter.ResourceServerFilterWrapper;
import org.apache.shiro.realm.Realm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Created by hanwen on 2017/6/8.
 */
@EnableConfigurationProperties(ResourceServerProperties.class)
@AutoConfigureBefore({ShiroFilterAutoConfiguration.class, ShiroAutoConfiguration.class})
@ConditionalOnClass(Realm.class)
public class ResourceServerFilterAutoConfiguration {

  @Autowired
  private ResourceServerProperties resourceProperties;

  @Value("${shiro.logoutUrl}")
  private String logoutUrl;

  @Bean
  public CustomFilterWrapper oauth2ResourceFilter() {
    ResourceServerFilterWrapper filter = new ResourceServerFilterWrapper();
    filter.setServerScheme(resourceProperties.getServerScheme());
    filter.setServerHost(resourceProperties.getServerHost());
    filter.setServerPort(resourceProperties.getServerPort());
    filter.setServerContextPath(resourceProperties.getServerContextPath());
    filter.setClientId(resourceProperties.getClientId());
    filter.setClientSecret(resourceProperties.getClientSecret());
    return filter;
  }

  @Bean
  public CustomFilterChainDefinition oauth2ResourceFilterChain() {
    return new ResourceServerFilterChainDefinition(logoutUrl);
  }
}
