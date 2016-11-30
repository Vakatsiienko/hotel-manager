package com.vaka.service;

import com.vaka.domain.BaseEntity;
import com.vaka.domain.Manager;

/**
 * Created by Iaroslav on 11/24/2016.
 */
public interface CrudService<T extends BaseEntity> {
    T create(T entity);

    T getById(Integer id);

    boolean delete(Integer id);

    T update(Integer id, T entity);

}
