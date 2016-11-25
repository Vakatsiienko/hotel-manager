package com.vaka.repository;

import com.vaka.domain.Room;
import com.vaka.domain.Bill;
import com.vaka.domain.User;

import java.time.LocalDate;

/**
 * Created by Iaroslav on 11/24/2016.
 */
public interface BillRepository {
    Bill createBill(User client, Room room, LocalDate startDate, LocalDate endDate);
}
