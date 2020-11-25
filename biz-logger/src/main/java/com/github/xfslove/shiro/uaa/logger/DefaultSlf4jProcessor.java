package com.github.xfslove.shiro.uaa.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by hanwen on 2020/11/24.
 */
public class DefaultSlf4jProcessor implements BizLoggerEntityProcessor {

  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSlf4jProcessor.class);

  @Override
  public void process(List<BizLoggerEntity> entityList) {
    String info = entityList.stream()
        .map(entity -> "\"" + entity.getType() + "\":\"" + entity.getValue().toString() + "\"")
        .collect(Collectors.joining(",", "{", "}"));

    LOGGER.info("BIZ LOGGER INFO : {}.", info);
  }
}
