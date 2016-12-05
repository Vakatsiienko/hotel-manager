package com.vaka.repository;

import com.vaka.domain.Reservation;
import com.vaka.domain.ReservationStatus;

import java.util.List;

/**
 * Created by Iaroslav on 11/27/2016.
 */
public interface ReservationRepository extends CrudRepository<Reservation> {

    List<Reservation> findByRoomIdAndStatus(Integer roomId, ReservationStatus status);

    //    List<Reservation> findByStatus();
//
//    List<Reservation> findRequested();
//
//    List<Reservation> findRejected();
    List<Reservation> findByStatus(ReservationStatus status);

}
