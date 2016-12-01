package com.vaka.repository;

import com.vaka.domain.Reservation;

import java.util.List;

/**
 * Created by Iaroslav on 11/27/2016.
 */
public interface ReservationRepository extends CrudRepository<Reservation> {
    List<Reservation> findByRoomId(Integer roomId);
    List<Reservation> findConfirmed();
    List<Reservation> findRequested();

}
