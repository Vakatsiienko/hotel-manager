package com.vaka.repository;

import com.vaka.domain.BaseEntity;

/**
 * Created by Iaroslav on 11/24/2016.
 */
public interface CrudRepository<T extends BaseEntity> {

    T create(T entity);

    T read(Long id);

    boolean delete(Long id);

    T update(Long id, T entity);

}
