package com.vaka.hotel_manager.service.impl;

import com.vaka.hotel_manager.context.ApplicationContext;
import com.vaka.hotel_manager.domain.DTO.ReservationDTO;
import com.vaka.hotel_manager.domain.Reservation;
import com.vaka.hotel_manager.domain.ReservationStatus;
import com.vaka.hotel_manager.domain.Room;
import com.vaka.hotel_manager.domain.User;
import com.vaka.hotel_manager.repository.ReservationRepository;
import com.vaka.hotel_manager.repository.RoomRepository;
import com.vaka.hotel_manager.service.BillService;
import com.vaka.hotel_manager.service.ReservationService;
import com.vaka.hotel_manager.service.SecurityService;
import com.vaka.hotel_manager.util.SecurityUtil;
import com.vaka.hotel_manager.util.exception.NotFoundException;
import org.slf4j.Logger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
    private Lock applyRoomLock = new ReentrantLock();

    @Override
    public boolean applyRoom(User loggedUser, Integer roomId, Integer reservationId) {
        LOG.debug("Trying to apply room with id: {} , by reservation with id: {}", roomId, reservationId);
        getSecurityService().authorize(loggedUser, SecurityUtil.MANAGER_ACCESS_ROLES);
        Optional<Room> room = getRoomRepository().getById(roomId);
        applyRoomLock.lock();
        Optional<Reservation> request = getReservationRepository().getById(reservationId);

        if (!room.isPresent() || !request.isPresent()) {
            applyRoomLock.unlock();
            throw new NotFoundException(String.format("Not found room or request by given id, founded room: %s, request: %s", room.get(), request.get()));
        }
        if (request.get().getStatus() != ReservationStatus.REQUESTED) {
            applyRoomLock.unlock();
            LOG.debug("Reservation in illegal state");//TODO throw illegalState exception
            return false;
        }
        boolean datesOverlap = getReservationRepository().existOverlapReservation(roomId, request.get().getArrivalDate(), request.get().getDepartureDate());
        if (datesOverlap)
            return false;
        request.get().setRoom(room.get());
        request.get().setStatus(ReservationStatus.CONFIRMED);
        update(loggedUser, reservationId, request.get());
        applyRoomLock.unlock();
        LOG.debug("Room applied and reservation confirmed");
        getBillService().createFromReservation(loggedUser, request.get());
        return true;
    }

    @Override
    public List<ReservationDTO> findActiveByUserId(User loggedUser, Integer userId) {
        LOG.debug("Looking reservations by userId: {}", userId);
        if (!loggedUser.getId().equals(userId))
            getSecurityService().authorize(loggedUser, SecurityUtil.MANAGER_ACCESS_ROLES);
        return getReservationRepository().findActiveByUserId(userId);
    }

    @Override
    public List<ReservationDTO> findByStatusFromDate(User loggedUser, ReservationStatus status, LocalDate fromDate) {
        LOG.debug("Searching reservations by status: {}, from date: {}", status, fromDate);
        getSecurityService().authorize(loggedUser, SecurityUtil.MANAGER_ACCESS_ROLES);
        return getReservationRepository().findByStatusFromDate(status, fromDate);
    }

    @Override
    public List<ReservationDTO> findByStatusAndUserId(User loggedUser, ReservationStatus status, Integer userId) {
        LOG.debug("Searching reservations by status: {}, and userId: {}", status, userId);
        if (!userId.equals(loggedUser.getId()))
            getSecurityService().authorize(loggedUser, SecurityUtil.MANAGER_ACCESS_ROLES);
        return getReservationRepository().findByUserIdAndStatus(userId, status);
    }

    @Override
    public boolean reject(User loggedUser, Integer reservationId) {
        LOG.debug("Rejecting reservationId: {}", reservationId);
        Optional<Reservation> reservationOptional = getReservationRepository().getById(reservationId);
        if (reservationOptional.isPresent()) {
            if (!reservationOptional.get().getUser().getId().equals(loggedUser.getId())) {
                getSecurityService().authorize(loggedUser, SecurityUtil.MANAGER_ACCESS_ROLES);
            }
            reservationOptional.get().setStatus(ReservationStatus.REJECTED);
            LOG.debug("Updating reservation: {}", reservationOptional.get());
            return reservationRepository.update(reservationId, reservationOptional.get());
        } else throw new NotFoundException("Reservation not found.");
    }

    @Override
    public Reservation create(User loggedUser, Reservation reservation) {
        reservation.setCreatedDatetime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        LOG.debug("Creating reservation: {}", reservation);
        return getReservationRepository().create(reservation);
    }

    @Override
    public Optional<Reservation> getById(User loggedUser, Integer id) {
        LOG.debug("Searching reservation with id: {}", id);
        Optional<Reservation> reservation = getReservationRepository().getById(id);
        if (reservation.isPresent()) {
            if (!reservation.get().getUser().getId().equals(loggedUser.getId())) {
                getSecurityService().authorize(loggedUser, SecurityUtil.MANAGER_ACCESS_ROLES);
            }
        }
        return reservation;
    }

    @Override
    public boolean update(User loggedUser, Integer id, Reservation reservation) {
        LOG.debug("Updating reservation with id: {}, updating state: {}", id, reservation);
        Optional<Reservation> reservationOptional = getById(loggedUser, id);
        if (reservationOptional.isPresent()) {
            return getReservationRepository().update(id, reservation);
        } else throw new NotFoundException("Reservation not found.");
    }

    @Override
    public boolean delete(User loggedUser, Integer id) {
        getSecurityService().authorize(loggedUser, SecurityUtil.MANAGER_ACCESS_ROLES);
        LOG.debug("Deleting reservation with id: {}", id);
        return getReservationRepository().delete(id);
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
}
