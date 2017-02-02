package com.vaka.hotel_manager.repository;

import com.vaka.hotel_manager.domain.Page;
import com.vaka.hotel_manager.domain.entities.Room;
import com.vaka.hotel_manager.domain.entities.RoomClass;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Created by Iaroslav on 11/24/2016.
 */
public interface RoomRepository extends CrudRepository<Room> {

    /**
     * @return all available rooms in given date period
     */
    List<Room> findAvailableForReservation(RoomClass roomClass, LocalDate arrivalDate, LocalDate departureDate);

    List<Room> findAll();

    boolean existsRoomByRoomClass(Integer roomClassId);

    Optional<Room> getByNumber(Integer number);

    Page<Room> findPage(Integer page, Integer rows);
}
