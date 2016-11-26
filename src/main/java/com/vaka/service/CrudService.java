package com.vaka.service;

import com.vaka.domain.BaseEntity;
import com.vaka.domain.User;

/**
 * Created by Iaroslav on 11/24/2016.
 */
public interface CrudService<T extends BaseEntity> {
    T create(User loggedUser, T entity);

    T getById(User loggedUser, Integer id);

    boolean delete(User loggedUser, Integer id);

    T update(User loggedUser, Integer id, T entity);

}
