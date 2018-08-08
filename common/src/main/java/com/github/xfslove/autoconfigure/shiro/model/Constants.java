package com.github.xfslove.autoconfigure.shiro.model;

/**
 * Created by hanwen on 05/01/2018.
 */
public class Constants {

  private Constants() {
  }

  /*=============
    uri path
   =============*/

  public static final String SERVER_AUTH_PATH = "/uaa-auth";

  public static final String SERVER_RESOURCE_PATH = "/uaa-resource";

  public static final String SERVER_AUTHORIZE_PATH = SERVER_AUTH_PATH + "/authorize";

  public static final String SERVER_ACCESS_TOKEN_PATH = SERVER_AUTH_PATH + "/access-token";

  public static final String SERVER_LOGOUT_PATH = SERVER_AUTH_PATH + "/logout";


  /*=============
    jwt claims
   =============*/

  public static final String ACCOUNT_ID = "aid";

  public static final String PERM_ROLE = "pro";

  public static final String PERM_CODE = "pcd";

  public static final String[] OAUTH2_JWT_CLAIMS = {ACCOUNT_ID, PERM_ROLE, PERM_CODE};
}
