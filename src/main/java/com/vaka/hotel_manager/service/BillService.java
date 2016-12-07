package com.vaka.hotel_manager.service;

import com.vaka.hotel_manager.domain.Bill;
import com.vaka.hotel_manager.domain.Reservation;
import com.vaka.hotel_manager.domain.User;

import java.util.Optional;

/**
 * Created by Iaroslav on 11/24/2016.
 */
public interface BillService extends CrudService<Bill> {

    void createFromReservation(User loggedUser, Reservation reservation);

    Optional<Bill> getBillByReservationId(User loggedUser, Integer reservationId);

}
