package com.vaka.hotel_manager.repository;

import com.vaka.hotel_manager.domain.entities.Bill;

import java.util.Optional;

/**
 * Created by Iaroslav on 11/24/2016.
 */
public interface BillRepository extends CrudRepository<Bill> {

    /**
     * @param id seeking bill
     * @return Optional.empty() if bill doesn't exist
     */
    Optional<Bill> getByReservationId(Integer id);
}
