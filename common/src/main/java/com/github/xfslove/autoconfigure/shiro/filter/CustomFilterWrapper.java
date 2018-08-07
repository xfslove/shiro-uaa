package com.github.xfslove.autoconfigure.shiro.filter;

import javax.servlet.Filter;

/**
 * Created by hanwen on 2017/10/10.
 */
public interface CustomFilterWrapper {

  Filter getFilter();

  String getName();
}
