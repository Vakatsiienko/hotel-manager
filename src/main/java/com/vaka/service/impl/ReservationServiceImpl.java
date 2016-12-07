package com.vaka.service.impl;

import com.vaka.context.ApplicationContext;
import com.vaka.domain.*;
import com.vaka.repository.BillRepository;
import com.vaka.repository.ReservationRepository;
import com.vaka.repository.RoomRepository;
import com.vaka.service.BillService;
import com.vaka.service.ReservationService;
import com.vaka.util.DateAndTimeUtil;
import com.vaka.util.exception.AuthorizationException;
import com.vaka.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

/**
 * Created by Iaroslav on 11/27/2016.
 */
public class ReservationServiceImpl implements ReservationService {
    private ReservationRepository reservationRepository;
    private RoomRepository roomRepository;
    private BillRepository billRepository;
    private BillService billService;

    @Override
    public boolean applyRoomForReservation(User loggedUser, Integer roomId, Integer reservationId) {
        if (loggedUser.getRole() != Role.MANAGER)
            throw new AuthorizationException("Not Allowed.");
        Optional<Room> room = getRoomRepository().getById(roomId);
        Optional<Reservation> request = getReservationRepository().getById(reservationId);

        if (!room.isPresent() || !request.isPresent())
            throw new NotFoundException(String.format("Not found room or request by given id, founded room: %s, request: %s", room, request));
        boolean datesOverlap = reservationRepository.findByRoomIdAndStatus(roomId, ReservationStatus.CONFIRMED).stream().filter(//TODO get boolean from bd, existsByRoomIdAndDates()
                reservation -> DateAndTimeUtil.areDatesOverlap(reservation.getArrivalDate(),
                        reservation.getDepartureDate(), request.get().getArrivalDate(), request.get().getDepartureDate())
        ).count() > 0;
        if (datesOverlap)
            return false;
        //TODO implement isolation(synchronization)
        request.get().setRoom(room.get());
        request.get().setStatus(ReservationStatus.CONFIRMED);
        getBillService().createFromReservation(loggedUser, request.get());
        update(loggedUser, reservationId, request.get());
        return true;
    }

    @Override
    public List<Reservation> findActiveByUserId(User loggedUser, Integer userId) {
        return getReservationRepository().findActiveByUserId(userId);
    }

    @Override
    public List<Reservation> findByStatus(User loggedUser, ReservationStatus status) {
        if (loggedUser.getRole() != Role.MANAGER)
            throw new AuthorizationException("Not Allowed.");
        return getReservationRepository().findByStatus(status);
    }

    @Override
    public List<Reservation> findByStatusAndUserId(User loggedUser, ReservationStatus status, Integer userId) {
        if (loggedUser.getRole() != Role.MANAGER || !userId.equals(loggedUser.getId()))
            throw new AuthorizationException("Not Allowed.");
        return getReservationRepository().findByUserIdAndStatus(userId, status);
    }

    @Override
    public boolean reject(User loggedUser, Integer reservationId) {
        Optional<Reservation> reservationOptional = getReservationRepository().getById(reservationId);
        if (reservationOptional.isPresent()) {
            if (loggedUser.getRole() == Role.MANAGER || reservationOptional.get().getUser().getId().equals(loggedUser.getId())) {
                Room room = new Room();
                room.setId(-1);
                reservationOptional.get().setRoom(room);
                reservationOptional.get().setStatus(ReservationStatus.REJECTED);
                reservationRepository.update(reservationId, reservationOptional.get());
                return true;
            } else throw new AuthorizationException("Not Allowed.");
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
        if (loggedUser.getRole() == Role.MANAGER) {
            return reservation;
        }
        if (reservation.isPresent()) {
            if (reservation.get().getUser().getId().equals(loggedUser.getId())) {
                return reservation;
            }
        }
        throw new AuthorizationException("Not Allowed.");
    }

    @Override
    public boolean update(User loggedUser, Integer id, Reservation entity) {
        if (loggedUser.getRole() == Role.MANAGER) {
            return getReservationRepository().update(id, entity);
        } else throw new AuthorizationException("Not Allowed.");
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
