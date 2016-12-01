package com.vaka.repository.inMemoryImpl;

import com.vaka.domain.Manager;
import com.vaka.repository.ManagerRepository;
import com.vaka.util.ApplicationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Iaroslav on 11/26/2016.
 */
public class ManagerRepositoryImpl implements ManagerRepository {
    private Map<Integer, Manager> userById = new ConcurrentHashMap<>();
    private AtomicInteger idCounter = ApplicationContext.getIdCounter();

    @Override
    public Manager create(Manager entity) {
        entity.setId(idCounter.getAndIncrement());
        userById.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Manager getByEmail(String email) {
        return userById.values().stream().filter(m -> m.getLogin().equals(email)).findFirst().get();
    }

    @Override
    public Manager getById(Integer id) {
        return userById.get(id);
    }

    @Override
    public boolean delete(Integer id) {
        return userById.remove(id) != null;
    }

    @Override
    public Manager update(Integer id, Manager entity) {
        entity.setId(id);
        userById.put(id, entity);
        return entity;
    }
}
