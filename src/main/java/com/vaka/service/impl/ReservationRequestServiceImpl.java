package com.vaka.service.impl;

import com.vaka.domain.ReservationRequest;
import com.vaka.domain.User;
import com.vaka.repository.ReservationRequestRepository;
import com.vaka.service.ReservationRequestService;
import com.vaka.util.ApplicationContext;

import java.util.List;

/**
 * Created by Iaroslav on 11/26/2016.
 */
public class ReservationRequestServiceImpl implements ReservationRequestService {
    private ReservationRequestRepository requestRepository;
    @Override
    public List<ReservationRequest> list(User loggedUser) {
        return getRequestRepository().list();
    }

    @Override
    public ReservationRequest create(User loggedUser, ReservationRequest entity) {
        return getRequestRepository().create(entity);
    }

    @Override
    public ReservationRequest getById(User loggedUser, Integer id) {
        return getRequestRepository().getById(id);
    }

    @Override
    public boolean delete(User loggedUser, Integer id) {
        return getRequestRepository().delete(id);
    }

    @Override
    public ReservationRequest update(User loggedUser, Integer id, ReservationRequest entity) {
        return getRequestRepository().update(id, entity);
    }

    public ReservationRequestRepository getRequestRepository() {
        if (requestRepository == null) {
            synchronized (this){
                if (requestRepository == null) {
                    requestRepository = ApplicationContext.getBean(ReservationRequestRepository.class);
                }
            }
        }
        return requestRepository;
    }
}
