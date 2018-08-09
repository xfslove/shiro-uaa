package com.github.xfslove.shiro.uaa;

import com.github.xfslove.shiro.uaa.model.AccessClient;
import com.github.xfslove.shiro.uaa.model.Account;
import com.github.xfslove.shiro.uaa.model.Role;
import com.github.xfslove.shiro.uaa.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.HashSet;

/**
 * Created by hanwen on 2018/8/8.
 */
@AutoConfigureBefore({AuthServerFilterAutoConfiguration.class, AuthServerShiroAutoConfiguration.class, AuthServerEndpointAutoConfiguration.class})
@EnableConfigurationProperties(AuthServerProperties.class)
public class AuthServerDefaultApiAutoConfiguration {

  @Autowired
  private AuthServerProperties properties;

  @Bean
  @ConditionalOnMissingBean(AccessClientService.class)
  public AccessClientService accessClientService() {
    MemoryAccessClientService clientService = new MemoryAccessClientService();
    for (AuthServerProperties.AccessClient cc : properties.getClients()) {
      AccessClient client = new AccessClient();
      client.setName(cc.getName());
      client.setClientId(cc.getClientId());
      client.setClientSecret(cc.getClientSecret());
      clientService.addClient(client);
    }
    return clientService;
  }

  @Bean
  @ConditionalOnMissingBean(AccessTokenService.class)
  public AccessTokenService accessTokenService() {
    return new MemoryAccessTokenService();
  }

  @Bean
  @ConditionalOnMissingBean(AuthCodeService.class)
  public AuthCodeService authCodeService() {
    return new MemoryAuthCodeService();
  }

  @Bean
  @ConditionalOnMissingBean(RoleService.class)
  public RoleService roleService(AccessClientService accessClientService) {
    MemoryRoleService roleService = new MemoryRoleService();
    for (AuthServerProperties.Role rc : properties.getRoles()) {
      AccessClient client = accessClientService.getByClient(rc.getClientId());
      Role role = new Role();
      role.setName(rc.getName());
      role.setAccessClientId(client.getId());
      role.setPermCodes(new HashSet<>(rc.getPermCodes()));
      roleService.addRole(role);
    }
    return roleService;
  }

  @Bean
  @ConditionalOnMissingBean(AccountService.class)
  public AccountService accountService(RoleService roleService) {
    MemoryAccountService accountService = new MemoryAccountService();
    accountService.setRoleService(roleService);
    for (AuthServerProperties.Account ac : properties.getAccounts()) {
      Account account = new Account();
      account.setUsername(ac.getUsername());
      account.setPassword(ac.getPassword());
      for (String role : ac.getRoles()) {
        account.getRoleIds().add(roleService.getByName(role).getId());
      }
      accountService.addAccount(account);
    }
    return accountService;
  }
}
