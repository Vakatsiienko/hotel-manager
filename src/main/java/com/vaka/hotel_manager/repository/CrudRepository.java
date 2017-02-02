package com.vaka.hotel_manager.repository;

import com.vaka.hotel_manager.domain.entity.BaseEntity;

import java.util.Optional;

/**
 * Created by Iaroslav on 11/24/2016.
 */
public interface CrudRepository<T extends BaseEntity> {

    T create(T entity);

    Optional<T> getById(Integer id);

    boolean delete(Integer id);

    boolean update(Integer id, T entity);

}
