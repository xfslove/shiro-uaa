package com.github.xfslove.shiro.uaa;

import com.github.xfslove.shiro.uaa.endpoint.LogoutEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;

/**
 * Created by hanwen on 05/01/2018.
 */
@AutoConfigureAfter(ResourceServerShiroAutoConfiguration.class)
public class ResourceServerEndpointAutoConfiguration {

  @Autowired
  private ResourceServerProperties resourceProperties;

  @Bean
  public LogoutEndpoint logoutEndpoint() {
    LogoutEndpoint logoutEndpoint = new LogoutEndpoint();
    logoutEndpoint.setServerScheme(resourceProperties.getServerScheme());
    logoutEndpoint.setServerHost(resourceProperties.getServerHost());
    logoutEndpoint.setServerPort(resourceProperties.getServerPort());
    logoutEndpoint.setServerContextPath(resourceProperties.getServerContextPath());
    logoutEndpoint.setClientId(resourceProperties.getClientId());
    return logoutEndpoint;
  }
}
