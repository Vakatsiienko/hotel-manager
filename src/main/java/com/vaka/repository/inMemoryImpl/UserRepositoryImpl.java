package com.vaka.repository.inMemoryImpl;

import com.vaka.domain.User;
import com.vaka.repository.UserRepository;
import com.vaka.util.ApplicationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Iaroslav on 11/26/2016.
 */
public class UserRepositoryImpl implements UserRepository {
    private Map<Integer, User> userById = new ConcurrentHashMap<>();
    private AtomicInteger idCounter = ApplicationContext.getIdCounter();

    @Override
    public User persist(User entity) {
        entity.setId(idCounter.getAndIncrement());
        userById.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public User getById(Integer id) {
        return userById.get(id);
    }

    @Override
    public boolean delete(Integer id) {
        return userById.remove(id) != null;
    }

    @Override
    public User update(Integer id, User entity) {
        entity.setId(id);
        userById.put(id, entity);
        return entity;
    }
}
