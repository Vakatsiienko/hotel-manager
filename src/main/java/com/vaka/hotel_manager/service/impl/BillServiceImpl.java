package com.vaka.hotel_manager.service.impl;

import com.vaka.hotel_manager.context.ApplicationContext;
import com.vaka.hotel_manager.domain.Bill;
import com.vaka.hotel_manager.domain.Reservation;
import com.vaka.hotel_manager.domain.Role;
import com.vaka.hotel_manager.domain.User;
import com.vaka.hotel_manager.repository.BillRepository;
import com.vaka.hotel_manager.service.BillService;
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
            if (!bill.get().getReservation().getUser().getId().equals(loggedUser.getId()))
                SecurityUtil.authorize(loggedUser, Role.MANAGER);
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
            if (bill.get().getReservation().getUser().getId().equals(loggedUser.getId()))
                SecurityUtil.authorize(loggedUser, Role.MANAGER);
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
