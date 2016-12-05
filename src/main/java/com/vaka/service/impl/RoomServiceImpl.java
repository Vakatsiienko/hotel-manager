package com.vaka.service.impl;

import com.vaka.domain.Reservation;
import com.vaka.domain.Role;
import com.vaka.domain.Room;
import com.vaka.domain.User;
import com.vaka.repository.ReservationRepository;
import com.vaka.repository.RoomRepository;
import com.vaka.service.RoomService;
import com.vaka.context.ApplicationContext;
import com.vaka.util.exception.AuthorizationException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Iaroslav on 11/26/2016.
 */
public class RoomServiceImpl implements RoomService {
    private RoomRepository roomRepository;
    private ReservationRepository reservationRepository;

    @Override
    public List<Room> findAvailableForReservation(User loggedUser, Integer reservationId) {
        Optional<Reservation> request = getReservationRepository().getById(reservationId);
        if (request.isPresent())
            return getRoomRepository().findAvailableForReservation(request.get().getRequestedRoomClass(),
                    request.get().getArrivalDate(), request.get().getDepartureDate());
        else return new ArrayList<>();
    }

    @Override
    public Room create(User loggedUser, Room entity) {
        entity.setCreatedDatetime(LocalDateTime.now());
         return getRoomRepository().create(entity);
    }

    @Override
    public Optional<Room> getById(User loggedUser, Integer id) {
        if (loggedUser.getRole() == Role.MANAGER)
            return getRoomRepository().getById(id);
        else throw new AuthorizationException("Not Allowed.");
    }

    @Override
    public boolean delete(User loggedUser, Integer id) {
        return getRoomRepository().delete(id);
    }

    @Override
    public boolean update(User loggedUser, Integer id, Room entity) {
        return getRoomRepository().update(id, entity);
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
}
