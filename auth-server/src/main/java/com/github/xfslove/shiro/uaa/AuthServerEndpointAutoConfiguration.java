package com.github.xfslove.shiro.uaa;

import com.github.xfslove.shiro.uaa.endpoint.UaaAccessTokenEndpoint;
import com.github.xfslove.shiro.uaa.endpoint.UaaAuthorizeEndpoint;
import com.github.xfslove.shiro.uaa.endpoint.UaaLogoutEndpoint;
import com.github.xfslove.shiro.uaa.service.AccessClientService;
import com.github.xfslove.shiro.uaa.service.AccessTokenService;
import com.github.xfslove.shiro.uaa.service.AccountService;
import com.github.xfslove.shiro.uaa.service.AuthCodeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * Created by hanwen on 2017/11/23.
 */
@AutoConfigureAfter(AuthServerShiroAutoConfiguration.class)
@EnableConfigurationProperties(AuthServerProperties.class)
public class AuthServerEndpointAutoConfiguration {

  @Value("${shiro.loginUrl}")
  private String loginUrl;

  @Bean
  public UaaAccessTokenEndpoint uaaAccessTokenEndpoint(ApplicationContext applicationContext, AuthServerProperties serverProperties) {
    UaaAccessTokenEndpoint uaaAccessTokenEndpoint = new UaaAccessTokenEndpoint();
    uaaAccessTokenEndpoint.setAccessTokenExpires(serverProperties.getAccessTokenExpires());
    uaaAccessTokenEndpoint.setRefreshTokenExpires(serverProperties.getRefreshTokenExpires());
    uaaAccessTokenEndpoint.setAccessTokenService(applicationContext.getBean(AccessTokenService.class));
    uaaAccessTokenEndpoint.setAccessClientService(applicationContext.getBean(AccessClientService.class));
    uaaAccessTokenEndpoint.setAccountService(applicationContext.getBean(AccountService.class));
    uaaAccessTokenEndpoint.setAuthCodeService(applicationContext.getBean(AuthCodeService.class));
    return uaaAccessTokenEndpoint;
  }

  @Bean
  public UaaAuthorizeEndpoint uaaAuthorizeEndpoint(ApplicationContext applicationContext, AuthServerProperties serverProperties) {
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
