package com.vaka.hotel_manager.service;

import com.vaka.hotel_manager.domain.entity.BaseEntity;
import com.vaka.hotel_manager.domain.entity.User;

import java.util.Optional;

/**
 * Created by Iaroslav on 11/24/2016.
 */
public interface CrudService<T extends BaseEntity> {
    T create(User loggedUser, T entity);

    Optional<T> getById(User loggedUser, Integer id);

    boolean delete(User loggedUser, Integer id);

    boolean update(User loggedUser, Integer id, T entity);

}
