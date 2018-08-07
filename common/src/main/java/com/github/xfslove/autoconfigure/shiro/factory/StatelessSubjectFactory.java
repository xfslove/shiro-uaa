package com.github.xfslove.autoconfigure.shiro.factory;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;

/**
 * Created by hanwen on 2017/9/20.
 */
public class StatelessSubjectFactory extends DefaultWebSubjectFactory {

  @Override
  public Subject createSubject(SubjectContext context) {
    // 是否创建session
    context.setSessionCreationEnabled(false);
    return super.createSubject(context);
  }
}
