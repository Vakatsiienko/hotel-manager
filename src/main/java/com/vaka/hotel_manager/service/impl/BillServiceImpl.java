package com.vaka.hotel_manager.service.impl;

import com.vaka.hotel_manager.context.ApplicationContext;
import com.vaka.hotel_manager.domain.Bill;
import com.vaka.hotel_manager.domain.Reservation;
import com.vaka.hotel_manager.domain.Role;
import com.vaka.hotel_manager.domain.User;
import com.vaka.hotel_manager.repository.BillRepository;
import com.vaka.hotel_manager.service.BillService;
import com.vaka.hotel_manager.service.SecurityService;
import com.vaka.hotel_manager.util.DomainFactory;
import com.vaka.hotel_manager.util.SecurityUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

/**
 * Created by Iaroslav on 11/26/2016.
 */
public class BillServiceImpl implements BillService {
    private static final Logger LOG = LoggerFactory.getLogger(BillServiceImpl.class);
    private SecurityService securityService;
    private BillRepository billRepository;

    @Override
    public void createForReservation(User loggedUser, Reservation reservation) {
        getSecurityService().authorize(loggedUser, SecurityUtil.MANAGER_ACCESS_ROLES);
        LOG.debug("Creating bill from reservation: {}", reservation);
        create(loggedUser, DomainFactory.createBillFromReservation(reservation));
    }

    @Override
    public Optional<Bill> getBillByReservationId(User loggedUser, Integer reservationId) {
        LOG.debug("reservationId: {}", reservationId);
        Optional<Bill> bill = getBillRepository().getByReservationId(reservationId);
        //if loggedUser is not owner of this bill and he don't have
        //appropriate role produce AuthorizationException
        if (bill.isPresent()) {
            if (!bill.get().getReservation().getUser().getId().equals(loggedUser.getId()))
                getSecurityService().authorize(loggedUser, SecurityUtil.MANAGER_ACCESS_ROLES);
        }
        return bill;
    }

    @Override
    public Bill create(User loggedUser, Bill bill) {
        getSecurityService().authorize(loggedUser, SecurityUtil.MANAGER_ACCESS_ROLES);
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
                getSecurityService().authorize(loggedUser, SecurityUtil.MANAGER_ACCESS_ROLES);
        }
        return bill;
    }

    @Override
    public boolean delete(User loggedUser, Integer id) {
        LOG.debug("Deleting bill with id: {}", id);
        getSecurityService().authorize(loggedUser, SecurityUtil.MANAGER_ACCESS_ROLES);
        return getBillRepository().delete(id);

    }

    @Override
    public boolean update(User loggedUser, Integer id, Bill bill) {
        LOG.debug("Updating bill with id: {} , bill: {}", id, bill);
        getSecurityService().authorize(loggedUser, SecurityUtil.MANAGER_ACCESS_ROLES);
        return getBillRepository().update(id, bill);

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
    public SecurityService getSecurityService() {
        if (securityService == null){
            synchronized (this){
                if (securityService == null) {
                    securityService = ApplicationContext.getInstance().getBean(SecurityService.class);
                }
            }
        }
        return securityService;
    }
}
