package com.vaka.repository;

import com.vaka.domain.Reservation;

import java.util.List;

/**
 * Created by Iaroslav on 11/27/2016.
 */
public interface ReservationRepository extends CrudRepository<Reservation> {

    List<Reservation> findConfirmedByRoomId(Integer roomId);

    List<Reservation> findConfirmed();

    List<Reservation> findRequested();

    List<Reservation> findRejected();

}
