package com.vaka.repository.inMemoryImpl;

import com.vaka.repository.SecurityRepository;

import java.util.Map;
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
    public Integer getByToken(String token) {
        return userIdByToken.get(token);
    }
}
