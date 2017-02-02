package com.vaka.hotel_manager.service.impl;

import com.vaka.hotel_manager.core.context.ApplicationContext;
import com.vaka.hotel_manager.core.security.SecurityService;
import com.vaka.hotel_manager.core.security.SecurityUtils;
import com.vaka.hotel_manager.core.tx.TransactionHelper;
import com.vaka.hotel_manager.core.tx.TransactionManager;
import com.vaka.hotel_manager.domain.dto.ReservationDTO;
import com.vaka.hotel_manager.domain.*;
import com.vaka.hotel_manager.domain.entity.Reservation;
import com.vaka.hotel_manager.domain.entity.Room;
import com.vaka.hotel_manager.domain.entity.RoomClass;
import com.vaka.hotel_manager.domain.entity.User;
import com.vaka.hotel_manager.repository.ReservationRepository;
import com.vaka.hotel_manager.repository.RoomClassRepository;
import com.vaka.hotel_manager.repository.RoomRepository;
import com.vaka.hotel_manager.service.BillService;
import com.vaka.hotel_manager.service.ReservationService;
import com.vaka.hotel_manager.util.exception.NotFoundException;
import org.slf4j.Logger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Iaroslav on 11/27/2016.
 */
public class ReservationServiceImpl implements ReservationService {
    private static final Logger LOG = getLogger(ReservationServiceImpl.class);
    private ReservationRepository reservationRepository;
    private RoomRepository roomRepository;
    private BillService billService;
    private SecurityService securityService;
    private RoomClassRepository roomClassRepository;
    private TransactionHelper transactionHelper;

    @Override
    public boolean applyRoom(User loggedUser, Integer roomId, Integer reservationId) {
        LOG.debug("Trying to apply room with id: {} , by reservation with id: {}", roomId, reservationId);
        getSecurityService().authorize(loggedUser, SecurityUtils.MANAGER_ACCESS_ROLES);
        return getTransactionHelper().doTransactional(TransactionManager.TRANSACTION_SERIALIZABLE, () -> {
            Optional<Room> room = getRoomRepository().getById(roomId);
            Optional<Reservation> request = getReservationRepository().getById(reservationId);

            if (!room.isPresent() || !request.isPresent()) {
                throw new NotFoundException(String.format("Not found room or request by given id, founded room: %s, request: %s", room.get(), request.get()));
            }
            if (request.get().getStatus() != ReservationStatus.REQUESTED) {
                throw new IllegalStateException(String.format("Reservation is in illegal state, expected status Requested, actual %s", request.get().getStatus()));
            }
            boolean datesOverlap = getReservationRepository().existOverlapReservations(roomId, request.get().getArrivalDate(), request.get().getDepartureDate());
            if (datesOverlap) {
                return false;
            }
            request.get().setRoom(room.get());
            request.get().setStatus(ReservationStatus.CONFIRMED);
            getReservationRepository().update(reservationId, request.get());
            LOG.debug("Room applied and reservation confirmed");

            getTransactionHelper().doInner(() -> getBillService().createForReservation(loggedUser, request.get()));
            return true;
        });
    }

    @Override
    public List<ReservationDTO> findActiveByUserId(User loggedUser, Integer userId) {
        LOG.debug("Searching active reservations by userId: {}", userId);
        if (!userId.equals(loggedUser.getId()))
            getSecurityService().authorize(loggedUser, SecurityUtils.MANAGER_ACCESS_ROLES);
        return getTransactionHelper().doTransactional(() -> getReservationRepository().findActiveByUserId(userId));
    }

    @Override
    public Page<ReservationDTO> findPageByStatusFromDate(User loggedUser, ReservationStatus status, LocalDate fromDate, Integer page, Integer size) {
        getSecurityService().authorize(loggedUser, SecurityUtils.MANAGER_ACCESS_ROLES);
        LOG.debug("Searching reservations by status: {}, from date: {}", status, fromDate);
        return getTransactionHelper().doTransactional(() -> getReservationRepository().findPageByStatusFromDate(status, fromDate, page, size));
    }

    @Override
    public Page<ReservationDTO> findPageActiveByRoomClassNameAndArrivalDate(User loggedUser, String roomClassName, LocalDate arrivalDate, Integer page, Integer size) {
        getSecurityService().authorize(loggedUser, SecurityUtils.MANAGER_ACCESS_ROLES);
        LOG.debug("Searching reservations by roomClass: {}, arrivalDate: {}", roomClassName, arrivalDate);
        return getTransactionHelper().doTransactional(() -> getReservationRepository().findActiveByRoomClassNameAndArrivalDate(roomClassName, arrivalDate, page, size));
    }

    @Override
    public List<ReservationDTO> findByStatusAndUserId(User loggedUser, ReservationStatus status, Integer userId) {
        LOG.debug("Searching reservations by status: {}, and userId: {}", status, userId);
        if (!userId.equals(loggedUser.getId()))
            getSecurityService().authorize(loggedUser, SecurityUtils.MANAGER_ACCESS_ROLES);
        return getTransactionHelper().doTransactional(() -> getReservationRepository().findByUserIdAndStatus(userId, status));
    }

    @Override
    public boolean reject(User loggedUser, Integer reservationId) {
        LOG.debug("Rejecting reservationId: {}", reservationId);
        return getTransactionHelper().doTransactional(TransactionManager.TRANSACTION_REPEATABLE_READ, () -> {
            Optional<Reservation> reservationOptional = getReservationRepository().getById(reservationId);
            if (reservationOptional.isPresent()) {
                if (!reservationOptional.get().getUser().getId().equals(loggedUser.getId())) {
                    getSecurityService().authorize(loggedUser, SecurityUtils.MANAGER_ACCESS_ROLES);
                }
                if (reservationOptional.get().getStatus() == ReservationStatus.REJECTED)
                    throw new IllegalStateException("Reservation already rejected");
                reservationOptional.get().setStatus(ReservationStatus.REJECTED);
                LOG.debug("Updating reservation: {}", reservationOptional.get());
                return reservationRepository.update(reservationId, reservationOptional.get());
            } else throw new NotFoundException("Reservation not found.");
        });

    }

    @Override
    public Reservation create(User loggedUser, Reservation reservation) {
        reservation.setCreatedDatetime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        LOG.debug("Creating reservation: {}", reservation);
        return getTransactionHelper().doTransactional(TransactionManager.TRANSACTION_REPEATABLE_READ, () -> {
            Optional<RoomClass> rc = getRoomClassRepository().getByName(reservation.getRequestedRoomClass().getName());
            if (!rc.isPresent())
                throw new NotFoundException("Such Room Class doesn't exist");
            reservation.setRequestedRoomClass(rc.get());
            reservation.setStatus(ReservationStatus.REQUESTED);
            return getReservationRepository().create(reservation);
        });
    }

    @Override
    public Optional<Reservation> getById(User loggedUser, Integer id) {
        LOG.debug("Searching reservation with id: {}", id);

        Optional<Reservation> reservation = getTransactionHelper().doTransactional(() -> getReservationRepository().getById(id));
        if (reservation.isPresent()) {
            if (!reservation.get().getUser().getId().equals(loggedUser.getId())) {
                getSecurityService().authorize(loggedUser, SecurityUtils.MANAGER_ACCESS_ROLES);
            }
        }
        return reservation;
    }

    @Override
    public boolean update(User loggedUser, Integer id, Reservation reservation) {
        LOG.debug("Updating reservation with id: {}, updating state: {}", id, reservation);
        return getTransactionHelper().doTransactional(() -> {
            Optional<Reservation> reservationOptional = getById(loggedUser, id);
            if (reservationOptional.isPresent()) {
                if (!reservation.getUser().getId().equals(loggedUser.getId()))
                    getSecurityService().authorize(loggedUser, SecurityUtils.MANAGER_ACCESS_ROLES);
                return getReservationRepository().update(id, reservation);
            } else return false;
        });

    }

    @Override
    public boolean delete(User loggedUser, Integer id) {
        getSecurityService().authorize(loggedUser, SecurityUtils.MANAGER_ACCESS_ROLES);
        LOG.debug("Deleting reservation with id: {}", id);
        return getTransactionHelper().doTransactional(() -> getReservationRepository().delete(id));
    }

    private ReservationRepository getReservationRepository() {
        if (reservationRepository == null) {
            synchronized (this) {
                if (reservationRepository == null)
                    reservationRepository = ApplicationContext.getInstance().getBean(ReservationRepository.class);
            }
        }
        return reservationRepository;
    }

    public RoomRepository getRoomRepository() {
        if (roomRepository == null) {
            synchronized (this) {
                if (roomRepository == null)
                    roomRepository = ApplicationContext.getInstance().getBean(RoomRepository.class);
            }
        }
        return roomRepository;
    }

    public BillService getBillService() {
        if (billService == null) {
            synchronized (this) {
                if (billService == null) {
                    billService = ApplicationContext.getInstance().getBean(BillService.class);
                }
            }
        }
        return billService;
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

    public RoomClassRepository getRoomClassRepository() {
        if (roomClassRepository == null) {
            synchronized (this) {
                if (roomClassRepository == null) {
                    roomClassRepository = ApplicationContext.getInstance().getBean(RoomClassRepository.class);
                }
            }
        }
        return roomClassRepository;
    }
}
