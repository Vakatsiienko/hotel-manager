package com.vaka.repository;

import com.vaka.domain.Reservation;
import com.vaka.domain.Room;
import com.vaka.domain.RoomClass;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Iaroslav on 11/24/2016.
 */
public interface RoomRepository extends CrudRepository<Room> {

    List<Room> findAvailableForReservation(RoomClass roomClass, LocalDate arrivalDate, LocalDate departureDate);
}
