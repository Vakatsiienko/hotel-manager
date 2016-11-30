package com.vaka.web.controller;

import com.vaka.domain.ReservationRequest;
import com.vaka.domain.Manager;
import com.vaka.service.ReservationRequestService;
import com.vaka.util.ApplicationContext;

import java.util.List;

/**
 * Created by Iaroslav on 11/25/2016.
 */
public class ReservationRequestController {

    private ReservationRequestService requestService;

    public List<ReservationRequest> list(Manager loggedUser) {
        return getRequestService().list(loggedUser);
    }

    public ReservationRequest getById(Integer id) {
        return getRequestService().getById(id);
    }

    public ReservationRequest create( ReservationRequest request) {
        return getRequestService().create(request);
    }

    public ReservationRequest update(Integer id, ReservationRequest request) {
        return getRequestService().update(id, request);
    }

    public boolean delete(Integer id) {
        return getRequestService().delete(id);
    }

    public ReservationRequestService getRequestService() {
        if (requestService == null)
            synchronized (ReservationRequestController.class) {
                if (requestService == null) {
                    requestService = ApplicationContext.getBean(ReservationRequestService.class);
                }
            }
        return requestService;
    }
}
