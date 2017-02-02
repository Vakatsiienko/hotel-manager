package com.vaka.hotel_manager.service;

import com.vaka.hotel_manager.domain.entities.Bill;
import com.vaka.hotel_manager.domain.entities.Reservation;
import com.vaka.hotel_manager.domain.entities.User;

import java.util.Optional;

/**
 * Created by Iaroslav on 11/24/2016.
 */
public interface BillService extends CrudService<Bill> {

    /**
     * creates Bill using given reservation date period and Room information(Cost per day)
     */
    Bill createForReservation(User loggedUser, Reservation reservation);

    /**
     * @return Optional.empty() if there are no Bill made by reservation with this id
     */
    Optional<Bill> getBillByReservationId(User loggedUser, Integer reservationId);

}
