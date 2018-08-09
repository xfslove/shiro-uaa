package com.github.xfslove.autoconfigure.shiro;

import com.github.xfslove.autoconfigure.shiro.filter.CustomFilterChainDefinition;
import com.github.xfslove.autoconfigure.shiro.filter.ModularCustomFilterChainDefinition;
import com.github.xfslove.autoconfigure.shiro.realm.NoopRealm;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.config.web.autoconfigure.ShiroWebAutoConfiguration;
import org.apache.shiro.spring.web.config.AbstractShiroWebConfiguration;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;

import java.util.*;

/**
 * Created by hanwen on 2017/9/20.
 */
@AutoConfigureBefore(ShiroWebAutoConfiguration.class)
@ConditionalOnClass(Realm.class)
public class ShiroAutoConfiguration extends AbstractShiroWebConfiguration implements ApplicationContextAware {

  private ApplicationContext applicationContext;

  @Bean
  @ConditionalOnMissingBean(Realm.class)
  public Realm noopRealm() {
    return new NoopRealm();
  }

  @Bean
  @Override
  protected ShiroFilterChainDefinition shiroFilterChainDefinition() {
    ModularCustomFilterChainDefinition filterChainDefinition = new ModularCustomFilterChainDefinition();

    Map<String, CustomFilterChainDefinition> beans = applicationContext.getBeansOfType(CustomFilterChainDefinition.class);
    List<CustomFilterChainDefinition> list = new ArrayList<>(beans.values());
    Collections.sort(list, new Comparator<CustomFilterChainDefinition>() {
      @Override
      public int compare(CustomFilterChainDefinition o1, CustomFilterChainDefinition o2) {
        return new CompareToBuilder().append(o1.getOrder(), o2.getOrder()).toComparison();
      }
    });
    filterChainDefinition.setFilterChainDefinitions(list);

    return filterChainDefinition;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }
}
