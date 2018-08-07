package com.github.xfslove.autoconfigure.shiro.filter;

import com.github.xfslove.autoconfigure.shiro.model.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;

/**
 * Created by hanwen on 2017/9/20.
 */
public class OAuth2ServerFilterChainDefinition implements CustomFilterChainDefinition {

  private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2ServerFilterChainDefinition.class);

  @Override
  public LinkedHashMap<String, String> getFilterChainMap() {
    LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

    filterChainDefinitionMap.put(Constants.SERVER_AUTH_PATH + "/**", "anon");
    filterChainDefinitionMap.put(Constants.SERVER_RESOURCE_PATH + "/**", "oauth-server");

    LOGGER.info("uaa auth-server filter chain definition: {} {}", Constants.SERVER_AUTH_PATH + "/**", "anon");
    LOGGER.info("uaa auth-server filter chain definition: {} {}", Constants.SERVER_RESOURCE_PATH + "/**", "oauth-server");

    return filterChainDefinitionMap;
  }

  @Override
  public int getOrder() {
    return HIGHEST_PRECEDENCE;
  }
}
