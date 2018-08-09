package com.github.xfslove.shiro.uaa;

import com.github.xfslove.shiro.uaa.filter.CustomFilterWrapper;
import com.github.xfslove.shiro.uaa.filter.ModularCustomFilter;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.AbstractShiroWebFilterConfiguration;
import org.apache.shiro.spring.web.config.ShiroWebFilterConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by hanwen on 2017/6/8.
 */
@AutoConfigureBefore({ShiroWebFilterConfiguration.class, ShiroAutoConfiguration.class})
@ConditionalOnClass(Realm.class)
public class ShiroFilterAutoConfiguration extends AbstractShiroWebFilterConfiguration implements ApplicationContextAware {

  private ApplicationContext applicationContext;

  @Bean
  @Override
  protected ShiroFilterFactoryBean shiroFilterFactoryBean() {
    ShiroFilterFactoryBean factoryBean = super.shiroFilterFactoryBean();

    ModularCustomFilter modularCustomFilter = new ModularCustomFilter();
    Map<String, CustomFilterWrapper> wrapperMap = applicationContext.getBeansOfType(CustomFilterWrapper.class);
    List<CustomFilterWrapper> list = new ArrayList<>(wrapperMap.values());
    modularCustomFilter.setFilterWrappers(list);

    factoryBean.setFilters(modularCustomFilter.getFilters());
    return factoryBean;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}
