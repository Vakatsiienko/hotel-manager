package com.vaka.service.impl;

import com.vaka.domain.Reservation;
import com.vaka.domain.User;
import com.vaka.repository.ReservationRepository;
import com.vaka.service.ReservationService;
import com.vaka.util.ApplicationContext;
import com.vaka.util.exception.AuthorizationException;

/**
 * Created by Iaroslav on 11/27/2016.
 */
public class ReservationServiceImpl implements ReservationService {
    private ReservationRepository reservationRepository;

    @Override
    public Reservation persist(User loggedUser, Reservation entity) {
        if (loggedUser.isAdmin())
            return getReservationRepository().persist(entity);
        else throw new AuthorizationException();

    }

    @Override
    public Reservation getById(User loggedUser, Integer id) {
        if (loggedUser.isAdmin())
            return getReservationRepository().getById(id);
        else throw new AuthorizationException();
    }

    @Override
    public boolean delete(User loggedUser, Integer id) {
        if (loggedUser.isAdmin())
            return getReservationRepository().delete(id);
        else throw new AuthorizationException();
    }

    @Override
    public Reservation update(User loggedUser, Integer id, Reservation entity) {
        if (loggedUser.isAdmin())
            return getReservationRepository().update(id, entity);
        else throw new AuthorizationException();
    }

    public ReservationRepository getReservationRepository() {
        if (reservationRepository == null) {
            synchronized (this) {
                if (reservationRepository == null)
                    reservationRepository = ApplicationContext.getBean(ReservationRepository.class);
            }
        }
        return reservationRepository;
    }
}
