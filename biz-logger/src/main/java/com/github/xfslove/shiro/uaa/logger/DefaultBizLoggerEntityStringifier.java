package com.github.xfslove.shiro.uaa.logger;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by hanwen on 2020/11/24.
 */
public class DefaultBizLoggerEntityStringifier implements BizLoggerEntityStringifier {

  @Override
  public String stringifier(List<BizLoggerEntity> entityList) {
    return entityList.stream()
        .map(entity -> entity.getType() + ":[" + entity.getValue().toString() + "]")
        .collect(Collectors.joining(","));
  }
}
