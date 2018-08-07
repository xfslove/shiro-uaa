package com.github.xfslove.autoconfigure.shiro.filter;

import org.apache.commons.collections.CollectionUtils;

import javax.servlet.Filter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hanwen on 2017/10/10.
 */
public class ModularCustomFilter {

  private List<CustomFilterWrapper> filterWrappers;

  public Map<String, Filter> getFilters() {
    if (CollectionUtils.isEmpty(filterWrappers)) {
      return Collections.emptyMap();
    }

    Map<String, Filter> filterMap = new HashMap<>();

    for (CustomFilterWrapper filterWrapper : filterWrappers) {
      filterMap.put(filterWrapper.getName(), filterWrapper.getFilter());
    }

    return filterMap;
  }

  public void setFilterWrappers(List<CustomFilterWrapper> filterWrappers) {
    this.filterWrappers = filterWrappers;
  }
}
