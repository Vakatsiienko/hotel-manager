package com.vaka.service.impl;

import com.vaka.domain.Bill;
import com.vaka.domain.Reservation;
import com.vaka.domain.User;
import com.vaka.repository.BillRepository;
import com.vaka.service.BillService;
import com.vaka.util.ApplicationContext;
import com.vaka.util.DomainFactory;
import com.vaka.util.exception.AuthorizationException;

/**
 * Created by Iaroslav on 11/26/2016.
 */
public class BillServiceImpl implements BillService {

    /**
     * Don't invoke variable directly, use {@link #getBillRepository()}
     */
    private BillRepository billRepository;

    @Override
    public Bill createFromReservationAndPersist(User loggedUser, Reservation reservation) {
        if (loggedUser.isAdmin())
            return persist(loggedUser, DomainFactory.createFromReservation(reservation));
        else throw new AuthorizationException();
    }

    @Override
    public Bill persist(User loggedUser, Bill entity) {
        if (loggedUser.isAdmin())
            return getBillRepository().persist(entity);
        else throw new AuthorizationException();

    }

    @Override
    public Bill getById(User loggedUser, Integer id) {
        if (loggedUser.isAdmin())
            return getBillRepository().getById(id);
        else throw new AuthorizationException();

    }

    @Override
    public boolean delete(User loggedUser, Integer id) {
        if (loggedUser.isAdmin())
            return getBillRepository().delete(id);
        else throw new AuthorizationException();

    }

    @Override
    public Bill update(User loggedUser, Integer id, Bill entity) {
        if (loggedUser.isAdmin())
            return getBillRepository().update(id, entity);
        else throw new AuthorizationException();

    }

    public BillRepository getBillRepository() {
        if (billRepository == null) {
            synchronized (this) {
                if (billRepository == null) {
                    billRepository = ApplicationContext.getBean(BillRepository.class);
                }
            }
        }
        return billRepository;
    }
}
