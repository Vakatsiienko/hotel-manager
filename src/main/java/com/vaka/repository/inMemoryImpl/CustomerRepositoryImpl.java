package com.vaka.repository.inMemoryImpl;

import com.vaka.domain.User;
import com.vaka.domain.User;
import com.vaka.repository.CustomerRepository;
import com.vaka.util.ApplicationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Iaroslav on 12/1/2016.
 */
public class CustomerRepositoryImpl implements CustomerRepository {
    private Map<Integer, User> customerById = new ConcurrentHashMap<>();
    private AtomicInteger idCounter = ApplicationContext.getIdCounter();

    @Override
    public User create(User entity) {
        entity.setId(idCounter.getAndIncrement());
        customerById.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public User getByEmail(String email) {
        return customerById.values().stream().filter(c -> c.getEmail().equals(email)).findFirst().get();
    }

    @Override
    public User getById(Integer id) {
        return customerById.get(id);
    }

    @Override
    public boolean delete(Integer id) {
        return customerById.remove(id) != null;
    }

    @Override
    public User update(Integer id, User entity) {
        entity.setId(id);
        customerById.put(id, entity);
        return entity;
    }
}
