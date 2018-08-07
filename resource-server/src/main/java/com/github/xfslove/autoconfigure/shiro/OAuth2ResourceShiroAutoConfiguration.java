package com.github.xfslove.autoconfigure.shiro;

import com.github.xfslove.autoconfigure.shiro.realm.OAuth2ResourceRealm;
import org.apache.shiro.realm.Realm;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

/**
 * Created by hanwen on 2017/9/26.
 */
@AutoConfigureBefore({ShiroAutoConfiguration.class, OAuth2ResourceFilterAutoConfiguration.class})
@ConditionalOnClass(Realm.class)
public class OAuth2ResourceShiroAutoConfiguration {

  @Bean
  public Realm oauth2ResourceRealm() {
    OAuth2ResourceRealm oAuth2ResourceRealm = new OAuth2ResourceRealm();
    oAuth2ResourceRealm.setName("oauth2ResourceRealm");
    return oAuth2ResourceRealm;
  }
}
