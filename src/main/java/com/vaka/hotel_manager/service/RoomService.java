package com.vaka.hotel_manager.service;

import com.vaka.hotel_manager.domain.Room;
import com.vaka.hotel_manager.domain.User;

import java.util.List;

/**
 * Created by Iaroslav on 11/24/2016.
 */
public interface RoomService extends CrudService<Room> {

    /**
     * @param loggedUser
     * @param reservationId reservation which parameters will be taken for filter
     * @return List of available rooms that match by reservation parameters
     */
    List<Room> findAvailableForReservation(User loggedUser, Integer reservationId);

    /**
     * @param loggedUser
     * @return all rooms
     */
    List<Room> findAll(User loggedUser);

}
