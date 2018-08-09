package com.github.xfslove.shiro.uaa.filter;

import org.springframework.core.Ordered;

import java.util.LinkedHashMap;

/**
 * Created by hanwen on 2017/9/20.
 */
public interface CustomFilterChainDefinition extends Ordered {

  LinkedHashMap<String, String> getFilterChainMap();
}
