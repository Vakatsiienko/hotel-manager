package com.vaka.service.impl;

import com.vaka.domain.ReservationRequest;
import com.vaka.domain.User;
import com.vaka.repository.ReservationRequestRepository;
import com.vaka.service.ReservationRequestService;
import com.vaka.util.ApplicationContext;
import com.vaka.util.exception.AuthorizationException;

import java.util.List;

/**
 * Created by Iaroslav on 11/26/2016.
 */
public class ReservationRequestServiceImpl implements ReservationRequestService {
    private ReservationRequestRepository requestRepository;
    @Override
    public List<ReservationRequest> list(User loggedUser) {
        if (loggedUser.isAdmin())
            return getRequestRepository().list();
        else throw new AuthorizationException();

    }

    @Override
    public ReservationRequest persist(User loggedUser, ReservationRequest entity) {
        if (loggedUser.isAdmin())
            return getRequestRepository().persist(entity);
        else throw new AuthorizationException();
    }

    @Override
    public ReservationRequest getById(User loggedUser, Integer id) {
        if (loggedUser.isAdmin())
            return getRequestRepository().getById(id);
        else throw new AuthorizationException();
    }

    @Override
    public boolean delete(User loggedUser, Integer id) {
        if (loggedUser.isAdmin())
            return getRequestRepository().delete(id);
        else throw new AuthorizationException();
    }

    @Override
    public ReservationRequest update(User loggedUser, Integer id, ReservationRequest entity) {
        if (loggedUser.isAdmin())
            return getRequestRepository().update(id, entity);
        else throw new AuthorizationException();
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
