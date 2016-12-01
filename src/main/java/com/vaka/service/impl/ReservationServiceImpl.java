package com.vaka.service.impl;

import com.vaka.domain.*;
import com.vaka.repository.ReservationRepository;
import com.vaka.repository.RoomRepository;
import com.vaka.service.ReservationService;
import com.vaka.util.ApplicationContext;
import com.vaka.util.DateUtil;
import com.vaka.util.exception.NotFoundException;
import com.vaka.util.exception.ReservationCreationException;

import java.util.List;

/**
 * Created by Iaroslav on 11/27/2016.
 */
public class ReservationServiceImpl implements ReservationService {
    private ReservationRepository reservationRepository;
    private RoomRepository roomRepository;

    @Override
    public Reservation create(Reservation entity) {
            return getReservationRepository().create(entity);

    }

    @Override
    public List<Reservation> findRequested() {
        return getReservationRepository().findRequested();
    }

    @Override
    public List<Reservation> findConfirmed() {
        return getReservationRepository().findConfirmed();
    }

    @Override
    public Reservation applyRoomForReservation(Integer roomId, Integer reservationId) {
        Room room = getRoomRepository().getById(roomId);
        Reservation request = getReservationRepository().getById(reservationId);

        if (room == null || request == null)
            throw new NotFoundException(String.format("Not found room or request by given id, founded room: %s, request: %s", room, request));
        boolean datesOverlap = reservationRepository.findByRoomId(roomId).stream().filter(//TODO get boolean from bd, existsByRoomIdAndDates()
                reservation -> DateUtil.areDatesOverlap(reservation.getArrivalDate(),
                        reservation.getDepartureDate(), request.getArrivalDate(), request.getDepartureDate())
        ).count() > 0;
        if (datesOverlap)
            throw new ReservationCreationException("Requested Reservation arrival or departure dates with active reservations dates conflict");//TODO look rrom confirmed reservations
        //TODO implement isolation(synchronization)
        request.setRoom(room);
        request.setStatus(ReservationStatus.CONFIRMED);
        return update(reservationId, request);
    }

    @Override
    public Reservation getById(Integer id) {
            return getReservationRepository().getById(id);
    }

    @Override
    public boolean delete(Integer id) {
            return getReservationRepository().delete(id);
    }

    @Override
    public Reservation update(Integer id, Reservation entity) {
            return getReservationRepository().update(id, entity);
    }

    private ReservationRepository getReservationRepository() {
        if (reservationRepository == null) {
            synchronized (this) {
                if (reservationRepository == null)
                    reservationRepository = ApplicationContext.getBean(ReservationRepository.class);
            }
        }
        return reservationRepository;
    }

    public RoomRepository getRoomRepository() {
        if (roomRepository == null) {
            synchronized (this) {
                if (roomRepository == null)
                    roomRepository = ApplicationContext.getBean(RoomRepository.class);
            }
        }
        return roomRepository;
    }

}
