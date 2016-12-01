package com.vaka.service.impl;

import com.vaka.domain.Reservation;
import com.vaka.domain.Room;
import com.vaka.repository.ReservationRepository;
import com.vaka.repository.RoomRepository;
import com.vaka.service.RoomService;
import com.vaka.util.ApplicationContext;

import java.util.List;

/**
 * Created by Iaroslav on 11/26/2016.
 */
public class RoomServiceImpl implements RoomService {
    private RoomRepository roomRepository;
    private ReservationRepository reservationRepository;

    @Override
    public List<Room> findAvailableForReservation(Integer reservationId) {
        Reservation request = getReservationRepository().getById(reservationId);
        return getRoomRepository().findAvailableForReservation(request.getRequestedRoomClass(),
                request.getArrivalDate(), request.getDepartureDate());
    }

    @Override
    public Room create(Room entity) {
        return getRoomRepository().create(entity);
    }

    @Override
    public Room getById(Integer id) {
        return getRoomRepository().getById(id);
    }

    @Override
    public boolean delete(Integer id) {
        return getRoomRepository().delete(id);
    }

    @Override
    public Room update(Integer id, Room entity) {
        return getRoomRepository().update(id, entity);
    }

    public RoomRepository getRoomRepository() {
        if (roomRepository == null) {
            synchronized (this) {
                if (roomRepository == null) {
                    roomRepository = ApplicationContext.getBean(RoomRepository.class);
                }
            }
        }
        return roomRepository;
    }

    public ReservationRepository getReservationRepository() {
        if (reservationRepository == null) {
            synchronized (this) {
                if (reservationRepository == null) {
                    reservationRepository = ApplicationContext.getBean(ReservationRepository.class);
                }
            }
        }
        return reservationRepository;
    }
}
