package com.vaka.repository;

import com.vaka.domain.BaseEntity;

/**
 * Created by Iaroslav on 11/24/2016.
 */
public interface CrudRepository<T extends BaseEntity> {

    T persist(T entity);

    T getById(Integer id);

    boolean delete(Integer id);

    T update(Integer id, T entity);

}
