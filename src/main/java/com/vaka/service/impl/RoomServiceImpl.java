package com.vaka.service.impl;

import com.vaka.domain.ReservationRequest;
import com.vaka.domain.Room;
import com.vaka.repository.ReservationRequestRepository;
import com.vaka.repository.RoomRepository;
import com.vaka.service.RoomService;
import com.vaka.util.ApplicationContext;

import java.util.List;

/**
 * Created by Iaroslav on 11/26/2016.
 */
public class RoomServiceImpl implements RoomService {
    private RoomRepository roomRepository;
    private ReservationRequestRepository requestRepository;

    @Override
    public List<Room> findForRequestId(Integer id) {
        ReservationRequest request = getRequestRepository().getById(id);
        return findForRequest(request);
    }

    @Override
    public List<Room> findForRequest(ReservationRequest request) {
        return getRoomRepository().findForRequest(request);
    }

    @Override
    public Room create(Room entity) {
        return getRoomRepository().persist(entity);
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

    public ReservationRequestRepository getRequestRepository() {
        if (requestRepository == null) {
            synchronized (this) {
                if (requestRepository == null) {
                    requestRepository = ApplicationContext.getBean(ReservationRequestRepository.class);
                }
            }
        }
        return requestRepository;
    }
}
