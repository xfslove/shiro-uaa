package com.github.xfslove.autoconfigure.shiro.service;

import com.github.xfslove.autoconfigure.shiro.model.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by hanwen on 2018/8/8.
 */
public class MemoryRoleService implements RoleService {

  private static final Logger LOGGER = LoggerFactory.getLogger(MemoryRoleService.class);

  private AtomicLong id = new AtomicLong(1);

  private Map<Long, Role> db = new ConcurrentHashMap<>(16);

  @Override
  public Role getById(Long id) {
    return db.get(id);
  }

  @Override
  public Role getByName(String name) {
    for (Role value : db.values()) {
      if (value.getName().equals(name)) {
        return value;
      }
    }
    return null;
  }

  public void addRole(Role role) {
    role.setId(id.getAndIncrement());
    db.put(role.getId(), role);
    LOGGER.info("UAA SERVER INFO : add role [{}] perm-codes: {} to memory", role.getName(), role.getPermCodes());
  }
}
