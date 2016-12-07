package com.vaka.repository;

import com.vaka.domain.User;

import java.util.Optional;

/**
 * Created by Iaroslav on 12/1/2016.
 */
public interface UserRepository extends CrudRepository<User> {
    Optional<User> getByEmail(String email);

//    boolean updatePassword(Integer userId, String password);
}
