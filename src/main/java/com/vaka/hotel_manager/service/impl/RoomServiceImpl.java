package com.vaka.hotel_manager.service.impl;

import com.vaka.hotel_manager.core.context.ApplicationContext;
import com.vaka.hotel_manager.core.tx.TransactionHelper;
import com.vaka.hotel_manager.domain.Reservation;
import com.vaka.hotel_manager.domain.Room;
import com.vaka.hotel_manager.domain.RoomClass;
import com.vaka.hotel_manager.domain.User;
import com.vaka.hotel_manager.repository.ReservationRepository;
import com.vaka.hotel_manager.repository.RoomRepository;
import com.vaka.hotel_manager.service.RoomService;
import com.vaka.hotel_manager.service.SecurityService;
import com.vaka.hotel_manager.util.SecurityUtil;
import com.vaka.hotel_manager.util.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

/**
 * Created by Iaroslav on 11/26/2016.
 */
public class RoomServiceImpl implements RoomService {
    private RoomRepository roomRepository;
    private ReservationRepository reservationRepository;
    private SecurityService securityService;
    private TransactionHelper transactionHelper;
    private static final Logger LOG = LoggerFactory.getLogger(RoomServiceImpl.class);

    @Override
    public List<Room> findAvailableByClassAndDates(User loggedUser, RoomClass roomClass,
                                                   LocalDate arrivalDate, LocalDate departureDate) {
        getSecurityService().authorize(loggedUser, SecurityUtil.ANONYMOUS_ACCESS_ROLES);
        LOG.debug("Finding available rooms by RoomClass and dates");
        return getTransactionHelper().doTransactional(() ->
                getRoomRepository().findAvailableForReservation(roomClass, arrivalDate, departureDate)
        );
    }

    @Override
    public List<Room> findAvailableForReservation(User loggedUser, Integer reservationId) {
        getSecurityService().authorize(loggedUser, SecurityUtil.CUSTOMER_ACCESS_ROLES);
        LOG.debug("Finding available rooms for reservation by reservationId: {}", reservationId);
        return getTransactionHelper().doTransactional(() -> {
            Optional<Reservation> request = getReservationRepository().getById(reservationId);
            if (request.isPresent()) {
                return getRoomRepository().findAvailableForReservation(request.get().getRequestedRoomClass(),
                        request.get().getArrivalDate(), request.get().getDepartureDate());
            } else throw new NotFoundException("ReservationNotFound");
        });
    }

    @Override
    public List<Room> findAll(User loggedUser) {
        LOG.debug("Finding all rooms");
        return getTransactionHelper().doTransactional(getRoomRepository()::findAll);
    }

    @Override
    public Room create(User loggedUser, Room room) {
        getSecurityService().authorize(loggedUser, SecurityUtil.MANAGER_ACCESS_ROLES);
        room.setCreatedDatetime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        LOG.debug("Creating room: {}", room);
        return getTransactionHelper().doTransactional(() -> getRoomRepository().create(room));
    }

    @Override
    public Optional<Room> getById(User loggedUser, Integer id) {
        LOG.debug("Getting room by id: {}", id);
        return getTransactionHelper().doTransactional(() -> getRoomRepository().getById(id));
    }

    @Override
    public boolean delete(User loggedUser, Integer id) {
        getSecurityService().authorize(loggedUser, SecurityUtil.MANAGER_ACCESS_ROLES);
        LOG.debug("Deleting room by id: {}", id);
        return getTransactionHelper().doTransactional(() -> getRoomRepository().delete(id));
    }

    @Override
    public boolean update(User loggedUser, Integer id, Room room) {
        getSecurityService().authorize(loggedUser, SecurityUtil.MANAGER_ACCESS_ROLES);
        LOG.debug("Updating room with id: {}, state: {}", id, room);
        return getTransactionHelper().doTransactional(() -> getRoomRepository().update(id, room));
    }

    public RoomRepository getRoomRepository() {
        if (roomRepository == null) {
            synchronized (this) {
                if (roomRepository == null) {
                    roomRepository = ApplicationContext.getInstance().getBean(RoomRepository.class);
                }
            }
        }
        return roomRepository;
    }

    public ReservationRepository getReservationRepository() {
        if (reservationRepository == null) {
            synchronized (this) {
                if (reservationRepository == null) {
                    reservationRepository = ApplicationContext.getInstance().getBean(ReservationRepository.class);
                }
            }
        }
        return reservationRepository;
    }

    public SecurityService getSecurityService() {
        if (securityService == null) {
            synchronized (this) {
                if (securityService == null) {
                    securityService = ApplicationContext.getInstance().getBean(SecurityService.class);
                }
            }
        }
        return securityService;
    }

    public TransactionHelper getTransactionHelper() {
        if (transactionHelper == null) {
            synchronized (this) {
                if (transactionHelper == null) {
                    transactionHelper = ApplicationContext.getInstance().getBean(TransactionHelper.class);
                }
            }
        }
        return transactionHelper;
    }
}
