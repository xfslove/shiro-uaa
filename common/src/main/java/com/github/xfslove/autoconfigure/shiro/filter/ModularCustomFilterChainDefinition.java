package com.github.xfslove.autoconfigure.shiro.filter;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hanwen on 2017/9/20.
 */
public class ModularCustomFilterChainDefinition implements ShiroFilterChainDefinition {

  private List<CustomFilterChainDefinition> filterChainDefinitions;

  @Override
  public Map<String, String> getFilterChainMap() {
    if (CollectionUtils.isEmpty(filterChainDefinitions)) {
      return Collections.emptyMap();
    }

    LinkedHashMap<String, String> filterChainMap = new LinkedHashMap<>();
    for (CustomFilterChainDefinition filterChainDefinition : filterChainDefinitions) {
      filterChainMap.putAll(filterChainDefinition.getFilterChainMap());
    }
    return filterChainMap;
  }

  public void setFilterChainDefinitions(List<CustomFilterChainDefinition> filterChainDefinitions) {
    this.filterChainDefinitions = filterChainDefinitions;
  }
}
