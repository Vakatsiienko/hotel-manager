package com.vaka.repository;

import com.vaka.domain.ReservationRequest;

import java.util.List;

/**
 * Created by Iaroslav on 11/25/2016.
 */
public interface ReservationRequestRepository  extends CrudRepository<ReservationRequest>{

    List<ReservationRequest> list();

}
