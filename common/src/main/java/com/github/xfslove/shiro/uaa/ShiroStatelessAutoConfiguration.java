package com.github.xfslove.shiro.uaa;

import com.github.xfslove.shiro.uaa.factory.StatelessSubjectFactory;
import org.apache.shiro.mgt.SessionStorageEvaluator;
import org.apache.shiro.mgt.SubjectFactory;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.config.web.autoconfigure.ShiroWebAutoConfiguration;
import org.apache.shiro.spring.web.config.AbstractShiroWebConfiguration;
import org.apache.shiro.web.mgt.DefaultWebSessionStorageEvaluator;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * don't enable this configuration, this for stateless supported in upcoming version.
 * <p>
 * Created by hanwen on 28/12/2017.
 */
@AutoConfigureBefore({ShiroFilterAutoConfiguration.class, ShiroAutoConfiguration.class, ShiroWebAutoConfiguration.class})
@ConditionalOnProperty(name = "shiro.stateless")
@ConditionalOnClass(Realm.class)
public class ShiroStatelessAutoConfiguration extends AbstractShiroWebConfiguration {

  @Bean
  @Override
  protected SubjectFactory subjectFactory() {
    return new StatelessSubjectFactory();
  }

  @Bean
  @Override
  protected SessionStorageEvaluator sessionStorageEvaluator() {
    DefaultWebSessionStorageEvaluator sessionStorageEvaluator = new DefaultWebSessionStorageEvaluator();
    sessionStorageEvaluator.setSessionStorageEnabled(false);
    return sessionStorageEvaluator;
  }

  @Bean
  @Override
  protected SessionManager sessionManager() {
    DefaultWebSessionManager sessionManager = (DefaultWebSessionManager) nativeSessionManager();
    sessionManager.setSessionValidationSchedulerEnabled(false);
    return sessionManager;
  }
}
