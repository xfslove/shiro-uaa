package com.github.xfslove.shiro.uaa.service;

import com.github.xfslove.shiro.uaa.model.Account;
import com.github.xfslove.shiro.uaa.model.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by hanwen on 2018/8/8.
 */
public class MemoryAccountService implements AccountService {

  private static final Logger LOGGER = LoggerFactory.getLogger(MemoryAccountService.class);

  private AtomicLong id = new AtomicLong(1);

  private Map<Long, Account> db = new ConcurrentHashMap<>(16);

  private RoleService roleService;

  @Override
  public Account getByUsername(String username) {
    for (Account value : db.values()) {
      if (value.getUsername().equals(username)) {
        return value;
      }
    }
    return null;
  }

  @Override
  public Account getById(Long id) {
    return db.get(id);
  }

  @Override
  public Set<String> getPermCodes(Long id, Long accessClientId) {
    Account account = getById(id);
    if (account == null) {
      return Collections.emptySet();
    }
    Set<String> perms = new HashSet<>();
    for (Long roleId : account.getRoleIds()) {
      Role role = roleService.getById(roleId);
      if (role.isEnabled() && role.getAccessClientId().equals(accessClientId)) {
        perms.addAll(role.getPermCodes());
      }
    }
    return perms;
  }

  @Override
  public Set<String> getPermRoles(Long id, Long accessClientId) {
    Account account = getById(id);
    if (account == null) {
      return Collections.emptySet();
    }
    Set<String> perms = new HashSet<>();
    for (Long roleId : account.getRoleIds()) {
      Role role = roleService.getById(roleId);
      if (role.isEnabled() && role.getAccessClientId().equals(accessClientId)) {
        perms.add(role.getName());
      }
    }
    return perms;
  }

  public void addAccount(Account account) {
    account.setId(id.getAndIncrement());
    db.put(account.getId(), account);
    LOGGER.info("UAA SERVER INFO : add account [{}] to memory", account.getUsername());
  }

  public void setRoleService(RoleService roleService) {
    this.roleService = roleService;
  }

}
