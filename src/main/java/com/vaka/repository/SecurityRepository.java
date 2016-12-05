package com.vaka.repository;

import java.util.Optional;

/**
 * Created by Iaroslav on 11/26/2016.
 */
public interface SecurityRepository {
    void create(String token, Integer userId);

    void delete(String token);

    Optional<Integer> getByToken(String token);
}
