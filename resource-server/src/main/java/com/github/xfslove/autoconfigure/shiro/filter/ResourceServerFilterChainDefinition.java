package com.github.xfslove.autoconfigure.shiro.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;

/**
 * Created by hanwen on 2017/9/20.
 */
public class ResourceServerFilterChainDefinition implements CustomFilterChainDefinition {

  private static final Logger LOGGER = LoggerFactory.getLogger(ResourceServerFilterChainDefinition.class);

  private static final String LOGGER_MSG = "Register";

  private String logoutUrl;

  public ResourceServerFilterChainDefinition(String logoutUrl) {
    this.logoutUrl = logoutUrl;
  }

  @Override
  public LinkedHashMap<String, String> getFilterChainMap() {
    LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

    filterChainDefinitionMap.put(logoutUrl, "anon");
    filterChainDefinitionMap.put("/**", "oauth-resource");

    LOGGER.info("uaa resource-server filter chain definition: {} {}", logoutUrl, "anon");
    LOGGER.info("uaa resource-server filter chain definition: {} {}", "/**", "oauth-resource");

    return filterChainDefinitionMap;
  }

  @Override
  public int getOrder() {
    return LOWEST_PRECEDENCE;
  }
}
