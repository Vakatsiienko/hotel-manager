package com.vaka.service;

import com.vaka.domain.Reservation;

import java.util.List;

/**
 * Created by Iaroslav on 11/27/2016.
 */
public interface ReservationService extends CrudService<Reservation> {

    Reservation applyRoomForReservation(Integer roomId, Integer requestId);

    List<Reservation> findConfirmed();

    List<Reservation> findRequested();
}
