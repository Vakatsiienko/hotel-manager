package com.vaka.hotel_manager.service;

import com.vaka.hotel_manager.domain.entity.RoomClass;
import com.vaka.hotel_manager.domain.entity.User;

import java.util.List;

/**
 * Created by Iaroslav on 1/22/2017.
 */
public interface RoomClassService extends CrudService<RoomClass> {
    List<RoomClass> findAll(User loggedUser);

}
