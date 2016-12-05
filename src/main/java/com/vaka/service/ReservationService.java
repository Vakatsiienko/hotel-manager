package com.vaka.service;

import com.vaka.domain.Reservation;
import com.vaka.domain.ReservationStatus;
import com.vaka.domain.User;

import java.util.List;

/**
 * Created by Iaroslav on 11/27/2016.
 */
public interface ReservationService extends CrudService<Reservation> {

    Reservation applyRoomForReservation(User loggedUser, Integer roomId, Integer requestId);

    List<Reservation> findByStatus(User loggedUser, ReservationStatus status);

//    List<Reservation> findRequested();
}
