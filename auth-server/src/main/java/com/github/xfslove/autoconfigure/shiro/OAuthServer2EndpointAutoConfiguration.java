package com.github.xfslove.autoconfigure.shiro;

import com.github.xfslove.autoconfigure.shiro.endpoint.UaaAuthorizeEndpoint;
import com.github.xfslove.autoconfigure.shiro.endpoint.UaaLogoutEndpoint;
import com.github.xfslove.autoconfigure.shiro.endpoint.UaaAccessTokenEndpoint;
import com.github.xfslove.autoconfigure.shiro.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * Created by hanwen on 2017/11/23.
 */
@AutoConfigureAfter(OAuth2ServerShiroAutoConfiguration.class)
@EnableConfigurationProperties(OAuth2ServerProperties.class)
public class OAuthServer2EndpointAutoConfiguration {

  @Value("${shiro.loginUrl}")
  private String loginUrl;

  @Autowired
  private OAuth2ServerProperties serverProperties;

  @Bean
  public UaaAccessTokenEndpoint uaaAccessTokenEndpoint(ApplicationContext applicationContext) {
    UaaAccessTokenEndpoint uaaAccessTokenEndpoint = new UaaAccessTokenEndpoint();
    uaaAccessTokenEndpoint.setAccessTokenExpires(serverProperties.getAccessTokenExpires());
    uaaAccessTokenEndpoint.setRefreshTokenExpires(serverProperties.getRefreshTokenExpires());
    uaaAccessTokenEndpoint.setAccessTokenService(applicationContext.getBean(AccessTokenService.class));
    uaaAccessTokenEndpoint.setAccessClientService(applicationContext.getBean(AccessClientService.class));
    uaaAccessTokenEndpoint.setRoleService(applicationContext.getBean(RoleService.class));
    uaaAccessTokenEndpoint.setAccountService(applicationContext.getBean(AccountService.class));
    uaaAccessTokenEndpoint.setAuthCodeService(applicationContext.getBean(AuthCodeService.class));
    return uaaAccessTokenEndpoint;
  }

  @Bean
  public UaaAuthorizeEndpoint uaaAuthorizeEndpoint(ApplicationContext applicationContext) {
    UaaAuthorizeEndpoint uaaAuthorizeEndpoint = new UaaAuthorizeEndpoint();
    uaaAuthorizeEndpoint.setLoginUrl(loginUrl);
    uaaAuthorizeEndpoint.setCodeExpires(serverProperties.getCodeExpires());
    uaaAuthorizeEndpoint.setAccessClientService(applicationContext.getBean(AccessClientService.class));
    uaaAuthorizeEndpoint.setAuthCodeService(applicationContext.getBean(AuthCodeService.class));
    return uaaAuthorizeEndpoint;
  }

  @Bean
  public UaaLogoutEndpoint uaaLogoutEndpoint(ApplicationContext applicationContext) {
    UaaLogoutEndpoint uaaLogoutEndpoint = new UaaLogoutEndpoint();
    uaaLogoutEndpoint.setAccessClientService(applicationContext.getBean(AccessClientService.class));
    uaaLogoutEndpoint.setAccessTokenService(applicationContext.getBean(AccessTokenService.class));
    return uaaLogoutEndpoint;
  }
}
