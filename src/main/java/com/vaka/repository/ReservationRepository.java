package com.vaka.repository;

import com.vaka.domain.Reservation;
import com.vaka.domain.ReservationStatus;

import java.util.List;

/**
 * Created by Iaroslav on 11/27/2016.
 */
public interface ReservationRepository extends CrudRepository<Reservation> {

    List<Reservation> findByRoomIdAndStatus(Integer roomId, ReservationStatus status);

    List<Reservation> findByStatus(ReservationStatus status);

    List<Reservation> findByUserIdAndStatus(Integer userId, ReservationStatus status);
    
    List<Reservation> findActiveByUserId(Integer userId);
}
