package com.vaka.hotel_manager.service.impl;

import com.vaka.hotel_manager.context.ApplicationContext;
import com.vaka.hotel_manager.domain.*;
import com.vaka.hotel_manager.repository.BillRepository;
import com.vaka.hotel_manager.repository.ReservationRepository;
import com.vaka.hotel_manager.repository.RoomRepository;
import com.vaka.hotel_manager.service.BillService;
import com.vaka.hotel_manager.service.ReservationService;
import com.vaka.hotel_manager.util.SecurityUtil;
import com.vaka.hotel_manager.util.exception.AuthorizationException;
import com.vaka.hotel_manager.util.exception.NotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

/**
 * Created by Iaroslav on 11/27/2016.
 */
public class ReservationServiceImpl implements ReservationService {
    private static final Logger LOG = LoggerFactory.getLogger(ReservationServiceImpl.class);
    private ReservationRepository reservationRepository;
    private RoomRepository roomRepository;
    private BillService billService;

    @Override
    public boolean applyRoomForReservation(User loggedUser, Integer roomId, Integer reservationId) {
        SecurityUtil.authorize(loggedUser, Role.MANAGER);
        Optional<Room> room = getRoomRepository().getById(roomId);
        Optional<Reservation> request = getReservationRepository().getById(reservationId);

        if (!room.isPresent() || !request.isPresent())
            throw new NotFoundException(String.format("Not found room or request by given id, founded room: %s, request: %s", room, request));
        if (request.get().getStatus() != ReservationStatus.REQUESTED)
            return false;
        synchronized (this) {
            boolean datesOverlap = getReservationRepository().existOverlapReservation(roomId, request.get().getArrivalDate(), request.get().getDepartureDate());
            if (datesOverlap)
                return false;
            //TODO implement isolation with transaction
            request.get().setRoom(room.get());
            request.get().setStatus(ReservationStatus.CONFIRMED);
            update(loggedUser, reservationId, request.get());
        }
        getBillService().createFromReservation(loggedUser, request.get());
        return true;
    }

    @Override
    public List<Reservation> findActiveByUserId(User loggedUser, Integer userId) {
        if (!loggedUser.getId().equals(userId))
            SecurityUtil.authorize(loggedUser, Role.MANAGER);
        return getReservationRepository().findActiveByUserId(userId);
    }

    @Override
    public List<Reservation> findByStatus(User loggedUser, ReservationStatus status) {
        SecurityUtil.authorize(loggedUser, Role.MANAGER);
        return getReservationRepository().findByStatus(status);
    }

    @Override
    public List<Reservation> findByStatusAndUserId(User loggedUser, ReservationStatus status, Integer userId) {
        if (!userId.equals(loggedUser.getId()))
            SecurityUtil.authorize(loggedUser, Role.MANAGER);
        return getReservationRepository().findByUserIdAndStatus(userId, status);
    }

    @Override
    public boolean reject(User loggedUser, Integer reservationId) {
        Optional<Reservation> reservationOptional = getReservationRepository().getById(reservationId);
        if (reservationOptional.isPresent()) {
            if (!reservationOptional.get().getUser().getId().equals(loggedUser.getId())) {
                SecurityUtil.authorize(loggedUser, Role.MANAGER);
            }
            reservationOptional.get().setStatus(ReservationStatus.REJECTED);
            reservationRepository.update(reservationId, reservationOptional.get());
            return true;
        } else return false;
    }

    @Override
    public Reservation create(User loggedUser, Reservation entity) {
        entity.setCreatedDatetime(LocalDateTime.now(ZoneId.of("UTC")).truncatedTo(ChronoUnit.SECONDS));
        return getReservationRepository().create(entity);
    }

    @Override
    public Optional<Reservation> getById(User loggedUser, Integer id) {
        Optional<Reservation> reservation = getReservationRepository().getById(id);
        if (reservation.isPresent()) {
            if (!reservation.get().getUser().getId().equals(loggedUser.getId())) {
                SecurityUtil.authorize(loggedUser, Role.MANAGER);
            }
        }
        return reservation;
    }

    @Override
    public boolean update(User loggedUser, Integer id, Reservation entity) {
        if (!entity.getUser().getId().equals(loggedUser.getId())) {
            SecurityUtil.authorize(loggedUser, Role.MANAGER);
        }
        return getReservationRepository().update(id, entity);
    }
    @Override
    public boolean delete(User loggedUser, Integer id) {
        if (loggedUser.getRole() == Role.MANAGER) {
            return getReservationRepository().delete(id);
        } else throw new AuthorizationException("Not Allowed.");
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

}
