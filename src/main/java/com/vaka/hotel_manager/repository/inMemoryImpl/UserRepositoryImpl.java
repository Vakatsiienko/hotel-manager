package com.vaka.hotel_manager.repository.inMemoryImpl;

import com.vaka.hotel_manager.context.ApplicationContext;
import com.vaka.hotel_manager.domain.User;
import com.vaka.hotel_manager.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Iaroslav on 12/1/2016.
 */
public class UserRepositoryImpl implements UserRepository {
    private Map<Integer, User> customerById = new ConcurrentHashMap<>();
    private AtomicInteger idCounter = ApplicationContext.getInstance().getIdCounter();

    @Override
    public User create(User entity) {
        entity.setId(idCounter.getAndIncrement());
        entity.setCreatedDatetime(LocalDateTime.now());
        customerById.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return customerById.values().stream().filter(c -> c.getEmail().equals(email)).findFirst();
    }

    @Override
    public Optional<User> getById(Integer id) {
        return Optional.of(customerById.get(id));
    }

    @Override
    public boolean delete(Integer id) {
        return customerById.remove(id) != null;
    }

    @Override
    public boolean update(Integer id, User entity) {
        entity.setId(id);
        if (customerById.containsKey(id)) {
            customerById.put(id, entity);
            return true;
        } else return false;
    }
}
