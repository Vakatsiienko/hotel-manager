package com.vaka.hotel_manager.repository;

import com.vaka.hotel_manager.domain.Room;
import com.vaka.hotel_manager.domain.RoomClass;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Iaroslav on 11/24/2016.
 */
public interface RoomRepository extends CrudRepository<Room> {

    /**
     * @return all available rooms in given date period
     */
    List<Room> findAvailableForReservation(RoomClass roomClass, LocalDate arrivalDate, LocalDate departureDate);
}
