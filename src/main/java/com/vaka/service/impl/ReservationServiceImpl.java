package com.vaka.service.impl;

import com.vaka.domain.Reservation;
import com.vaka.domain.Manager;
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
    public Reservation create(Reservation entity) {
            return getReservationRepository().persist(entity);

    }

    @Override
    public Reservation getById(Integer id) {
            return getReservationRepository().getById(id);
    }

    @Override
    public boolean delete(Integer id) {
            return getReservationRepository().delete(id);
    }

    @Override
    public Reservation update(Integer id, Reservation entity) {
            return getReservationRepository().update(id, entity);
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
