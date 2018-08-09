package com.github.xfslove.shiro.uaa;

import com.github.xfslove.shiro.uaa.realm.LoginRealm;
import com.github.xfslove.shiro.uaa.service.AccountService;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.config.web.autoconfigure.ShiroWebAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * Created by hanwen on 2017/10/20.
 */
@AutoConfigureBefore({ShiroFilterAutoConfiguration.class, ShiroAutoConfiguration.class, ShiroWebAutoConfiguration.class})
@ConditionalOnClass(Realm.class)
public class AuthServerShiroAutoConfiguration {

  @Bean
  public Realm loginRealm(ApplicationContext applicationContext) {
    LoginRealm loginRealm = new LoginRealm();
    loginRealm.setAccountService(applicationContext.getBean(AccountService.class));
    loginRealm.setCredentialsMatcher(applicationContext.getBean(CredentialsMatcher.class));
    return loginRealm;
  }
}
