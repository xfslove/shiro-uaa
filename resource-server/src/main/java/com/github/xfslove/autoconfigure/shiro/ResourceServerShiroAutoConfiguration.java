package com.github.xfslove.autoconfigure.shiro;

import com.github.xfslove.autoconfigure.shiro.realm.ResourceServerRealm;
import org.apache.shiro.realm.Realm;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

/**
 * Created by hanwen on 2017/9/26.
 */
@AutoConfigureBefore({ShiroAutoConfiguration.class, ResourceServerFilterAutoConfiguration.class})
@ConditionalOnClass(Realm.class)
public class ResourceServerShiroAutoConfiguration {

  @Bean
  public Realm oauth2ResourceRealm() {
    ResourceServerRealm resourceServerRealm = new ResourceServerRealm();
    resourceServerRealm.setName("oauth2ResourceRealm");
    return resourceServerRealm;
  }
}
