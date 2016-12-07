package com.vaka.hotel_manager.repository.inMemoryImpl;

import com.vaka.hotel_manager.repository.SecurityRepository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Iaroslav on 11/26/2016.
 */
public class SecurityRepositoryImpl implements SecurityRepository {
    private Map<String, Integer> userIdByToken = new ConcurrentHashMap<>();

    @Override
    public void create(String token, Integer userId) {
        userIdByToken.put(token, userId);
    }

    @Override
    public void delete(String token) {
        userIdByToken.remove(token);

    }

    @Override
    public Optional<Integer> getByToken(String token) {
        if (userIdByToken.containsKey(token))
            return Optional.of(userIdByToken.get(token));
        else return Optional.empty();
    }
}
