package com.vaka.repository;

import com.vaka.domain.User;
import com.vaka.domain.User;

/**
 * Created by Iaroslav on 12/1/2016.
 */
public interface CustomerRepository extends CrudRepository<User> {
    User getByEmail(String email);
}
