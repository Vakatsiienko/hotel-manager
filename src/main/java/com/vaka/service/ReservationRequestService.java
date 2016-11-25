package com.vaka.service;

import com.vaka.domain.ReservationRequest;

import java.util.List;

/**
 * Created by Iaroslav on 11/25/2016.
 */
public interface ReservationRequestService {

    List<ReservationRequest> list();

    ReservationRequest getById(Integer id);

    ReservationRequest create(ReservationRequest request);

}
