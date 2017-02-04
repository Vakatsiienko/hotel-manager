package com.vaka.hotel_manager.service.impl;

import com.vaka.hotel_manager.core.context.ApplicationContextHolder;
import com.vaka.hotel_manager.core.security.SecurityService;
import com.vaka.hotel_manager.core.security.SecurityUtils;
import com.vaka.hotel_manager.core.tx.TransactionManager;
import com.vaka.hotel_manager.domain.entity.Bill;
import com.vaka.hotel_manager.domain.entity.Reservation;
import com.vaka.hotel_manager.domain.entity.User;
import com.vaka.hotel_manager.repository.BillRepository;
import com.vaka.hotel_manager.repository.exception.ConstraintViolationException;
import com.vaka.hotel_manager.service.BillService;
import com.vaka.hotel_manager.util.exception.ApplicationException;
import com.vaka.hotel_manager.util.exception.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

/**
 * Created by Iaroslav on 11/26/2016.
 */
public class BillServiceImpl implements BillService {
    private static final Logger LOG = LoggerFactory.getLogger(BillServiceImpl.class);
    private SecurityService securityService;
    private BillRepository billRepository;
    private TransactionManager transactionManager;

    @Override
    public Bill createForReservation(User loggedUser, Reservation reservation) {
        getSecurityService().authorize(loggedUser, SecurityUtils.MANAGER_ACCESS_ROLES);
        LOG.debug("Creating bill from reservation: {}", reservation);
        Bill bill = new Bill();//TODO move creating bill to fabric or builder
        bill.setReservation(reservation);
        bill.setTotalCost(caltucateTotalCost(reservation));
        bill.setCreatedDatetime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        try {
            return getBillRepository().create(bill);
        } catch (RepositoryException e) {
            if (e instanceof ConstraintViolationException) {
                throw new IllegalArgumentException("Bill for this reservation already created");
            } else {
                LOG.error(e.getMessage(), e);
                throw new ApplicationException(e);
            }
        }
    }


    private int caltucateTotalCost(Reservation reservation) {
        return (int) (reservation.getRoom().getCostPerDay() *
                (reservation.getDepartureDate().toEpochDay() - reservation.getArrivalDate().toEpochDay()));
    }

    @Override
    public Optional<Bill> getBillByReservationId(User loggedUser, Integer reservationId) {
        LOG.debug("reservationId: {}", reservationId);
        Optional<Bill> bill = getBillRepository().getByReservationId(reservationId);
        //if loggedUser is not owner of this bill and he don't have
        //appropriate role produce AuthorizationException
        bill.ifPresent(bill1 -> {
            if (!bill1.getReservation().getUser().getId().equals(loggedUser.getId()))
                getSecurityService().authorize(loggedUser, SecurityUtils.MANAGER_ACCESS_ROLES);
        });
        return bill;
    }

    @Override
    public Bill create(User loggedUser, Bill bill) {
        getSecurityService().authorize(loggedUser, SecurityUtils.MANAGER_ACCESS_ROLES);
        bill.setCreatedDatetime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        LOG.debug("Creating bill: {}", bill);
        return getBillRepository().create(bill);
    }

    @Override
    public Optional<Bill> getById(User loggedUser, Integer id) {
        LOG.debug("Searching bill with id: {}", id);
        Optional<Bill> bill = getBillRepository().getById(id);
        if (bill.isPresent()) {
            if (bill.get().getReservation().getUser().getId().equals(loggedUser.getId()))
                getSecurityService().authorize(loggedUser, SecurityUtils.MANAGER_ACCESS_ROLES);
        }
        return bill;
    }

    @Override
    public boolean delete(User loggedUser, Integer id) {
        LOG.debug("Deleting bill with id: {}", id);
        getSecurityService().authorize(loggedUser, SecurityUtils.MANAGER_ACCESS_ROLES);
        return getBillRepository().delete(id);
    }

    @Override
    public boolean update(User loggedUser, Integer id, Bill bill) {
        LOG.debug("Updating bill with id: {} , bill: {}", id, bill);
        getSecurityService().authorize(loggedUser, SecurityUtils.MANAGER_ACCESS_ROLES);
        return getBillRepository().update(id, bill);
    }

    public BillRepository getBillRepository() {
        if (billRepository == null) {
            billRepository = ApplicationContextHolder.getContext().getBean(BillRepository.class);
        }
        return billRepository;
    }

    public SecurityService getSecurityService() {
        if (securityService == null) {
            securityService = ApplicationContextHolder.getContext().getBean(SecurityService.class);
        }
        return securityService;
    }

    public TransactionManager getTransactionManager() {
        if (transactionManager == null) {
            transactionManager = ApplicationContextHolder.getContext().getBean(TransactionManager.class);
        }
        return transactionManager;
    }
}
