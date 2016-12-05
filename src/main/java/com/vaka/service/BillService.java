package com.vaka.service;

import com.vaka.domain.Bill;
import com.vaka.domain.Reservation;
import com.vaka.domain.User;

/**
 * Created by Iaroslav on 11/24/2016.
 */
public interface BillService extends CrudService<Bill> {
    void createFromReservation(User loggedUser, Reservation reservation);

}
