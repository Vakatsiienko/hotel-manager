package com.vaka.hotel_manager.service;

import com.vaka.hotel_manager.domain.Bill;
import com.vaka.hotel_manager.domain.Reservation;
import com.vaka.hotel_manager.domain.User;

import java.util.Optional;

/**
 * Created by Iaroslav on 11/24/2016.
 */
public interface BillService extends CrudService<Bill> {

    /**
     * creates Bill using given reservation date period and Room information(Cost per day)
     */
    void createForReservation(User loggedUser, Reservation reservation);

    /**
     * @return Optional.empty() if there are no Bill made by reservation with this id
     */
    Optional<Bill> getBillByReservationId(User loggedUser, Integer reservationId);

}
