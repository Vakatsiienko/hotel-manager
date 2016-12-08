package com.vaka.hotel_manager.repository;

import java.util.Optional;

/**
 * Created by Iaroslav on 11/26/2016.
 */
public interface SecurityRepository {


    void create(String token, Integer userId);

    void delete(String token);

    /**
     * @return userId by given token, or Optional.empty() if token ended or doesn't exist
     */
    Optional<Integer> getByToken(String token);
}
