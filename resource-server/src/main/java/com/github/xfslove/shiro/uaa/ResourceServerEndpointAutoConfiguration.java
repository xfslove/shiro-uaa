package com.github.xfslove.shiro.uaa;

import com.github.xfslove.shiro.uaa.endpoint.LogoutEndpoint;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Created by hanwen on 05/01/2018.
 */
@EnableConfigurationProperties(ResourceServerProperties.class)
@AutoConfigureAfter(ResourceServerShiroAutoConfiguration.class)
public class ResourceServerEndpointAutoConfiguration {

  @Bean
  public LogoutEndpoint logoutEndpoint(ResourceServerProperties resourceProperties) {
    LogoutEndpoint logoutEndpoint = new LogoutEndpoint();
    logoutEndpoint.setServerScheme(resourceProperties.getServerScheme());
    logoutEndpoint.setServerHost(resourceProperties.getServerHost());
    logoutEndpoint.setServerPort(resourceProperties.getServerPort());
    logoutEndpoint.setServerContextPath(resourceProperties.getServerContextPath());
    logoutEndpoint.setClientId(resourceProperties.getClientId());
    return logoutEndpoint;
  }
}
