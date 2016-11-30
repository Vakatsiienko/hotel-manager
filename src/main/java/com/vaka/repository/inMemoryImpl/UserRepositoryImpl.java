package com.vaka.repository.inMemoryImpl;

import com.vaka.domain.Manager;
import com.vaka.repository.UserRepository;
import com.vaka.util.ApplicationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Iaroslav on 11/26/2016.
 */
public class UserRepositoryImpl implements UserRepository {
    private Map<Integer, Manager> userById = new ConcurrentHashMap<>();
    private AtomicInteger idCounter = ApplicationContext.getIdCounter();

    @Override
    public Manager persist(Manager entity) {
        entity.setId(idCounter.getAndIncrement());
        userById.put(entity.getId(), entity);
        return entity;
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
