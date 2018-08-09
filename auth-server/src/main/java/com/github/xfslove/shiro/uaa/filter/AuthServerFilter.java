package com.github.xfslove.shiro.uaa.filter;

import com.github.xfslove.shiro.uaa.service.AccessTokenService;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 当前没用
 * Created by hanwen on 2017/9/20.
 */
public class AuthServerFilter extends AccessControlFilter {

  private AccessTokenService accessTokenService;

  @Override
  protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
    return false;
  }

  @Override
  protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
//    HttpServletResponse httpResponse = WebUtils.toHttp(response);
//
//    try {
//      // must has access_token to get resources
//      String token = request.getParameter(OAuth.OAUTH_ACCESS_TOKEN);
//
//      if (StringUtils.isBlank(token)) {
//        throw OAuthProblemException.error(OAuthError.ResourceResponse.INVALID_REQUEST, "missing access_token");
//      }
//
//      AccessToken accessToken = accessTokenService.getByAccessToken(token);
//      if (accessToken == null) {
//        throw OAuthProblemException.error(OAuthError.ResourceResponse.INVALID_REQUEST, "access_token invalid");
//      }
//
//      if (System.currentTimeMillis() > accessToken.getAccessTokenExpires().getTime()) {
//        throw OAuthProblemException.error(OAuthError.ResourceResponse.EXPIRED_TOKEN, "access_token expired");
//      }
//
//    } catch (OAuthProblemException ex) {
//
//      OAuthResponse r = OAuthResponse.errorResponse(HttpServletResponse.SC_UNAUTHORIZED).error(ex).buildJSONMessage();
//      httpResponse.setStatus(r.getResponseStatus());
//      httpResponse.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE + ";charset=utf-8");
//      PrintWriter pw = httpResponse.getWriter();
//      pw.print(r.getBody());
//      pw.flush();
//      pw.close();
//
//      return false;
//    }
//
    return true;
  }

  public void setAccessTokenService(AccessTokenService accessTokenService) {
    this.accessTokenService = accessTokenService;
  }
}
