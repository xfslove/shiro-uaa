package com.github.xfslove.shiro.uaa.logger;

import java.util.List;

/**
 * Created by hanwen on 2020/11/24.
 */
public interface BizLoggerEntityProcessor {

  void process(List<BizLoggerEntity> entityList);
}
