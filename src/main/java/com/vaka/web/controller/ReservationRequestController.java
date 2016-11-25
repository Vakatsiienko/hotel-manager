package com.vaka.web.controller;

import com.vaka.domain.ReservationRequest;
import com.vaka.service.ReservationRequestService;
import com.vaka.util.ApplicationContext;
import com.vaka.web.ResponseEntity;

import java.util.List;

/**
 * Created by Iaroslav on 11/25/2016.
 */
public class ReservationRequestController {

    private ReservationRequestService requestService;

    public ResponseEntity<List<ReservationRequest>> list() {
        List<ReservationRequest> list = getRequestService().list();
        return new ResponseEntity<>(list, list.size());
    }

    public ResponseEntity<ReservationRequest> getById(Integer id){
        return new ResponseEntity<>(getRequestService().getById(id));
    }

    public ResponseEntity<ReservationRequest> create(ReservationRequest request) {
        return new ResponseEntity<>(getRequestService().create(request));
    }

    public ReservationRequestService getRequestService() {
        if (requestService == null)
            synchronized (ReservationRequestController.class){
                if (requestService == null) {
                    requestService = ApplicationContext.getBean(ReservationRequestService.class);
                }
            }
        return requestService;
    }
}
