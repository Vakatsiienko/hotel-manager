package com.vaka.hotel_manager.service.impl;

import com.vaka.hotel_manager.context.ApplicationContext;
import com.vaka.hotel_manager.domain.Reservation;
import com.vaka.hotel_manager.domain.Role;
import com.vaka.hotel_manager.domain.Room;
import com.vaka.hotel_manager.domain.User;
import com.vaka.hotel_manager.repository.ReservationRepository;
import com.vaka.hotel_manager.repository.RoomRepository;
import com.vaka.hotel_manager.service.RoomService;
import com.vaka.hotel_manager.util.exception.AuthorizationException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
        entity.setCreatedDatetime(LocalDateTime.now(ZoneId.of("UTC")).truncatedTo(ChronoUnit.SECONDS));
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
