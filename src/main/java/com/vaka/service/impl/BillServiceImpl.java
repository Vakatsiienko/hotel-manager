package com.vaka.service.impl;

import com.vaka.context.ApplicationContext;
import com.vaka.domain.Bill;
import com.vaka.domain.Reservation;
import com.vaka.domain.Role;
import com.vaka.domain.User;
import com.vaka.repository.BillRepository;
import com.vaka.service.BillService;
import com.vaka.util.DomainFactory;
import com.vaka.util.SecurityUtil;
import com.vaka.util.exception.AuthorizationException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

/**
 * Created by Iaroslav on 11/26/2016.
 */
public class BillServiceImpl implements BillService {


    private BillRepository billRepository;

    @Override
    public void createFromReservation(User loggedUser, Reservation reservation) {
        SecurityUtil.authorize(loggedUser, Role.MANAGER);
        create(loggedUser, DomainFactory.createBillFromReservation(reservation));
    }

    @Override
    public Optional<Bill> getBillByReservationId(User loggedUser, Integer reservationId) {
        Optional<Bill> bill = getBillRepository().getByReservationId(reservationId);
        if (bill.isPresent()) {
            if (bill.get().getReservation().getUser().getId().equals(loggedUser.getId()) || loggedUser.getRole() == Role.MANAGER)
                return bill;
            else throw new AuthorizationException("Not allowed");
        }
        return bill;
    }

    @Override
    public Bill create(User loggedUser, Bill entity) {
        SecurityUtil.authorize(loggedUser, Role.MANAGER);
        entity.setCreatedDatetime(LocalDateTime.now(ZoneId.of("UTC")).truncatedTo(ChronoUnit.SECONDS));
        return getBillRepository().create(entity);
    }

    @Override
    public Optional<Bill> getById(User loggedUser, Integer id) {
        Optional<Bill> bill = getBillRepository().getById(id);
        if (bill.isPresent()) {
            if (bill.get().getReservation().getUser().getId().equals(loggedUser.getId()) || loggedUser.getRole() == Role.MANAGER) {
                return bill;
            } else throw new AuthorizationException("Not allowed");
        }
        return bill;
    }

    @Override
    public boolean delete(User loggedUser, Integer id) {
        SecurityUtil.authorize(loggedUser, Role.MANAGER);
        return getBillRepository().delete(id);

    }

    @Override
    public boolean update(User loggedUser, Integer id, Bill entity) {
        SecurityUtil.authorize(loggedUser, Role.MANAGER);
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
