package com.vaka.service.impl;

import com.vaka.context.ApplicationContext;
import com.vaka.domain.Bill;
import com.vaka.domain.Reservation;
import com.vaka.domain.Role;
import com.vaka.domain.User;
import com.vaka.repository.BillRepository;
import com.vaka.service.BillService;
import com.vaka.util.DomainExtractor;
import com.vaka.util.exception.AuthorizationException;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Created by Iaroslav on 11/26/2016.
 */
public class BillServiceImpl implements BillService {

    /**
     * Don't invoke variable directly, use {@link #getBillRepository()}
     */
    private BillRepository billRepository;

    @Override
    public void createFromReservation(User loggedUser, Reservation reservation) {
        create(loggedUser, DomainExtractor.createBillFromReservation(reservation));
    }

    @Override
    public Bill create(User loggedUser, Bill entity) {
        entity.setCreatedDatetime(LocalDateTime.now());
        return getBillRepository().create(entity);
    }

    @Override
    public Optional<Bill> getById(User loggedUser, Integer id) {
        Optional<Bill> bill = getBillRepository().getById(id);
        if (loggedUser.getRole() == Role.MANAGER) {
            return bill;
        }
        if (bill.isPresent()) {
            if (bill.get().getReservation().getUser().getId().equals(loggedUser.getId())) {
                return bill;
            }
        }
        throw new AuthorizationException("Not Allowed.");
    }

    @Override
    public boolean delete(User loggedUser, Integer id) {
        return getBillRepository().delete(id);

    }

    @Override
    public boolean update(User loggedUser, Integer id, Bill entity) {
        return getBillRepository().update(id, entity);

    }

    public BillRepository getBillRepository() {
        if (billRepository == null) {
            synchronized (this) {
                if (billRepository == null) {
                    billRepository = ApplicationContext.getInstance().getBean(BillRepository.class);
                }
            }
        }
        return billRepository;
    }
}
