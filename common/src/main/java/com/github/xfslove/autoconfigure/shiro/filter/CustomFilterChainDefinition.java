package com.github.xfslove.autoconfigure.shiro.filter;

import org.springframework.core.Ordered;

import java.util.LinkedHashMap;

/**
 * Created by hanwen on 2017/9/20.
 */
public interface CustomFilterChainDefinition extends Ordered {

  LinkedHashMap<String, String> getFilterChainMap();
}
