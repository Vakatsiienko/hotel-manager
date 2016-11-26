package com.vaka.service;

import com.vaka.domain.ReservationRequest;
import com.vaka.domain.User;

import java.util.List;

/**
 * Created by Iaroslav on 11/25/2016.
 */
public interface ReservationRequestService extends CrudService<ReservationRequest>{

    List<ReservationRequest> list(User loggedUser);



}
