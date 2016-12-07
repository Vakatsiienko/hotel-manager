package com.vaka.hotel_manager.service;

import com.vaka.hotel_manager.domain.Reservation;
import com.vaka.hotel_manager.domain.ReservationStatus;
import com.vaka.hotel_manager.domain.User;

import java.util.List;

/**
 * Created by Iaroslav on 11/27/2016.
 */
public interface ReservationService extends CrudService<Reservation> {

    boolean applyRoomForReservation(User loggedUser, Integer roomId, Integer requestId);

    List<Reservation> findByStatus(User loggedUser, ReservationStatus status);

    List<Reservation> findByStatusAndUserId(User loggedUser, ReservationStatus status, Integer userId);

    boolean reject(User loggedUser, Integer reservationId);

    List<Reservation> findActiveByUserId(User loggedUser, Integer userId);
}
