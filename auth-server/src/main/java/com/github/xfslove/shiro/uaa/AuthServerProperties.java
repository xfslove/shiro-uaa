package com.github.xfslove.shiro.uaa;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Created by hanwen on 2017/9/20.
 */
@ConfigurationProperties(prefix = "shiro.uaa.server")
public class AuthServerProperties {

  /**
   * seconds
   */
  private Long accessTokenExpires = 60L * 5;

  /**
   * seconds
   */
  private Long refreshTokenExpires = 60L * 30;

  /**
   * seconds
   */
  private Long codeExpires = 60L * 5;

  private List<AccessClient> clients;

  private List<Role> roles;

  private List<Account> accounts;

  private String forwardErrorUrl;

  public Long getAccessTokenExpires() {
    return accessTokenExpires;
  }

  public void setAccessTokenExpires(Long accessTokenExpires) {
    this.accessTokenExpires = accessTokenExpires;
  }

  public Long getRefreshTokenExpires() {
    return refreshTokenExpires;
  }

  public void setRefreshTokenExpires(Long refreshTokenExpires) {
    this.refreshTokenExpires = refreshTokenExpires;
  }

  public void setCodeExpires(Long codeExpires) {
    this.codeExpires = codeExpires;
  }

  public Long getCodeExpires() {
    return codeExpires;
  }

  public List<AccessClient> getClients() {
    return clients;
  }

  public void setClients(List<AccessClient> clients) {
    this.clients = clients;
  }

  public List<Role> getRoles() {
    return roles;
  }

  public void setRoles(List<Role> roles) {
    this.roles = roles;
  }

  public List<Account> getAccounts() {
    return accounts;
  }

  public void setAccounts(List<Account> accounts) {
    this.accounts = accounts;
  }

  public String getForwardErrorUrl() {
    return forwardErrorUrl;
  }

  public void setForwardErrorUrl(String forwardErrorUrl) {
    this.forwardErrorUrl = forwardErrorUrl;
  }

  public static class AccessClient {

    private String name;

    private String clientId;

    private String clientSecret;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getClientId() {
      return clientId;
    }

    public void setClientId(String clientId) {
      this.clientId = clientId;
    }

    public String getClientSecret() {
      return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
      this.clientSecret = clientSecret;
    }
  }

  public static class Role {

    private String name;

    private List<String> permCodes;

    private String clientId;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public List<String> getPermCodes() {
      return permCodes;
    }

    public void setPermCodes(List<String> permCodes) {
      this.permCodes = permCodes;
    }

    public String getClientId() {
      return clientId;
    }

    public void setClientId(String clientId) {
      this.clientId = clientId;
    }
  }

  public static class Account {

    private String username;

    private String password;

    private List<String> roles;

    public String getUsername() {
      return username;
    }

    public void setUsername(String username) {
      this.username = username;
    }

    public String getPassword() {
      return password;
    }

    public void setPassword(String password) {
      this.password = password;
    }

    public List<String> getRoles() {
      return roles;
    }

    public void setRoles(List<String> roles) {
      this.roles = roles;
    }
  }
}
