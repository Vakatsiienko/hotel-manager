package com.vaka.repository;

/**
 * Created by Iaroslav on 11/26/2016.
 */
public interface SecurityRepository {
    void create(String token, Integer userId);

    void delete(String token);

    Integer getByToken(String token);
}
