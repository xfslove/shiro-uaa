package com.github.xfslove.autoconfigure.shiro;

import com.github.xfslove.autoconfigure.shiro.endpoint.LogoutEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;

/**
 * Created by hanwen on 05/01/2018.
 */
@AutoConfigureAfter(OAuth2ResourceShiroAutoConfiguration.class)
public class OAuth2ResourceEndpointAutoConfiguration {

  @Autowired
  private OAuth2ResourceProperties resourceProperties;

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
