package com.github.xfslove.shiro.uaa.filter;

import com.github.xfslove.shiro.uaa.model.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;

/**
 * Created by hanwen on 2017/9/20.
 */
public class AuthServerFilterChainDefinition implements CustomFilterChainDefinition {

  private static final Logger LOGGER = LoggerFactory.getLogger(AuthServerFilterChainDefinition.class);

  @Override
  public LinkedHashMap<String, String> getFilterChainMap() {
    LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

    filterChainDefinitionMap.put(Constants.SERVER_AUTH_PATH + "/**", "anon");
//    filterChainDefinitionMap.put(Constants.SERVER_RESOURCE_PATH + "/**", "auth-server");

    LOGGER.info("uaa auth-server filter chain definition: {} {}", Constants.SERVER_AUTH_PATH + "/**", "anon");
//    LOGGER.info("uaa auth-server filter chain definition: {} {}", Constants.SERVER_RESOURCE_PATH + "/**", "auth-server");

    return filterChainDefinitionMap;
  }

  @Override
  public int getOrder() {
    return LOWEST_PRECEDENCE;
  }
}
