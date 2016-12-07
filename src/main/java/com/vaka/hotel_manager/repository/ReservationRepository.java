package com.vaka.hotel_manager.repository;

import com.vaka.hotel_manager.domain.Reservation;
import com.vaka.hotel_manager.domain.ReservationStatus;

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
