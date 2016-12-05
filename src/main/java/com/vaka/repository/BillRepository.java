package com.vaka.repository;

import com.vaka.domain.Bill;

import java.util.Optional;

/**
 * Created by Iaroslav on 11/24/2016.
 */
public interface BillRepository extends CrudRepository<Bill> {
    Optional<Bill> getByReservationId(Integer id);
}
