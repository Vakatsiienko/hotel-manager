package com.vaka.repository;

import com.vaka.domain.Manager;

/**
 * Created by Iaroslav on 11/26/2016.
 */
public interface ManagerRepository extends CrudRepository<Manager> {
    Manager getByEmail(String email);

}
