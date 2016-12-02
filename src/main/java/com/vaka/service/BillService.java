package com.vaka.service;

import com.vaka.domain.Bill;
import com.vaka.domain.Reservation;

/**
 * Created by Iaroslav on 11/24/2016.
 */
public interface BillService extends CrudService<Bill> {
    Bill createFromReservation(Reservation reservation);

}
