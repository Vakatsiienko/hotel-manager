package com.vaka.hotel_manager.service;

import com.vaka.hotel_manager.domain.Room;
import com.vaka.hotel_manager.domain.User;

import java.util.List;

/**
 * Created by Iaroslav on 11/24/2016.
 */
public interface RoomService extends CrudService<Room> {

    List<Room> findAvailableForReservation(User loggedUser, Integer reservationId);
    
}
