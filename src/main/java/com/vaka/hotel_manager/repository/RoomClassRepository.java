package com.vaka.hotel_manager.repository;

import com.vaka.hotel_manager.domain.entity.RoomClass;

import java.util.List;
import java.util.Optional;

/**
 * Created by Iaroslav on 1/21/2017.
 */
public interface RoomClassRepository extends CrudRepository<RoomClass> {
    List<RoomClass> findAll();

    Optional<RoomClass> getByName(String name);
}
