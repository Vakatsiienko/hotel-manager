package com.vaka.web.controller;

import com.vaka.domain.ReservationRequest;
import com.vaka.domain.User;
import com.vaka.service.ReservationRequestService;
import com.vaka.util.ApplicationContext;

import java.util.List;

/**
 * Created by Iaroslav on 11/25/2016.
 */
public class ReservationRequestController {

    private ReservationRequestService requestService;

    public List<ReservationRequest> list(User loggedUser) {
        return getRequestService().list(loggedUser);
    }

    public ReservationRequest getById(User loggedUser, Integer id) {
        return getRequestService().getById(loggedUser, id);
    }

    public ReservationRequest create(User loggedUser, ReservationRequest request) {
        return getRequestService().persist(loggedUser, request);
    }

    public ReservationRequest update(User loggedUser, Integer id, ReservationRequest request) {
        return getRequestService().update(loggedUser, id, request);
    }

    public boolean delete(User loggedUser, Integer id) {
        return getRequestService().delete(loggedUser, id);
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
