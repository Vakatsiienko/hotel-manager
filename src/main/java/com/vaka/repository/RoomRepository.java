package com.vaka.repository;

import com.vaka.domain.Room;
import com.vaka.domain.ReservationRequest;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Iaroslav on 11/24/2016.
 */
public interface RoomRepository extends CrudRepository<Room> {

    List<Room> find(ReservationRequest reservationRequest);

    boolean reserve(Room room);
}
