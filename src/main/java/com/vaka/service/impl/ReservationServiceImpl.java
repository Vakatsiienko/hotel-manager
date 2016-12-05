package com.vaka.service.impl;

import com.vaka.context.ApplicationContext;
import com.vaka.domain.*;
import com.vaka.repository.ReservationRepository;
import com.vaka.repository.RoomRepository;
import com.vaka.service.ReservationService;
import com.vaka.util.DateUtil;
import com.vaka.util.exception.AuthorizationException;
import com.vaka.util.exception.NotFoundException;
import com.vaka.util.exception.ReservationCreationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Created by Iaroslav on 11/27/2016.
 */
public class ReservationServiceImpl implements ReservationService {
    private ReservationRepository reservationRepository;
    private RoomRepository roomRepository;

    @Override
    public Reservation create(User loggedUser, Reservation entity) {
        entity.setCreatedDatetime(LocalDateTime.now());
        return getReservationRepository().create(entity);
    }

    @Override
    public List<Reservation> findByStatus(User loggedUser, ReservationStatus status) {
        if (loggedUser.getRole() != Role.MANAGER)
            throw new AuthorizationException("Not Allowed.");
        return getReservationRepository().findByStatus(status);
    }

    @Override
    public Reservation applyRoomForReservation(User loggedUser, Integer roomId, Integer reservationId) {
        if (loggedUser.getRole() != Role.MANAGER)
            throw new AuthorizationException("Not Allowed.");
        Optional<Room> room = getRoomRepository().getById(roomId);
        Optional<Reservation> request = getReservationRepository().getById(reservationId);

        if (!room.isPresent() || !request.isPresent())
            throw new NotFoundException(String.format("Not found room or request by given id, founded room: %s, request: %s", room, request));
        boolean datesOverlap = reservationRepository.findByRoomIdAndStatus(roomId, ReservationStatus.CONFIRMED).stream().filter(//TODO get boolean from bd, existsByRoomIdAndDates()
                reservation -> DateUtil.areDatesOverlap(reservation.getArrivalDate(),
                        reservation.getDepartureDate(), request.get().getArrivalDate(), request.get().getDepartureDate())
        ).count() > 0;
        if (datesOverlap)
            throw new ReservationCreationException("Requested Reservation arrival or departure dates with active reservations dates conflict");//TODO look rrom confirmed reservations
        //TODO implement isolation(synchronization)
        request.get().setRoom(room.get());
        request.get().setStatus(ReservationStatus.CONFIRMED);
        update(loggedUser, reservationId, request.get());
        return request.get();//TODO change to boolean
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
    public boolean delete(User loggedUser, Integer id) {
        if (loggedUser.getRole() == Role.MANAGER) {
            return getReservationRepository().delete(id);
        } else throw new AuthorizationException("Not Allowed.");
    }

    @Override
    public boolean update(User loggedUser, Integer id, Reservation entity) {
        if (loggedUser.getRole() == Role.MANAGER) {
            return getReservationRepository().update(id, entity);
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

}
