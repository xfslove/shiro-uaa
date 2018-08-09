package com.github.xfslove.shiro.uaa.service;

import com.github.xfslove.shiro.uaa.model.AccessClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by hanwen on 2018/8/8.
 */
public class MemoryAccessClientService implements AccessClientService {

  private static final Logger LOGGER = LoggerFactory.getLogger(MemoryAccessClientService.class);

  private AtomicLong id = new AtomicLong(1);

  private Map<Long, AccessClient> db = new ConcurrentHashMap<>(16);

  @Override
  public AccessClient getById(Long id) {
    return db.get(id);
  }

  @Override
  public AccessClient getByClient(String clientId) {
    for (AccessClient value : db.values()) {
      if (value.getClientId().equals(clientId)) {
        return value;
      }
    }
    return null;
  }

  public void addClient(AccessClient client) {
    client.setId(id.getAndIncrement());
    db.put(client.getId(), client);
    LOGGER.info("UAA SERVER INFO : add access client [{}] to memory", client.getClientId());
  }
}
