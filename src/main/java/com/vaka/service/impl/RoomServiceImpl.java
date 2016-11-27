package com.vaka.service.impl;

import com.vaka.domain.ReservationRequest;
import com.vaka.domain.Room;
import com.vaka.domain.User;
import com.vaka.repository.ReservationRequestRepository;
import com.vaka.repository.RoomRepository;
import com.vaka.service.RoomService;
import com.vaka.util.ApplicationContext;
import com.vaka.util.exception.AuthorizationException;

import java.util.List;

/**
 * Created by Iaroslav on 11/26/2016.
 */
public class RoomServiceImpl implements RoomService {
    private RoomRepository roomRepository;
    private ReservationRequestRepository requestRepository;

    @Override
    public List<Room> findForRequestId(User loggedUser, Integer id) {
        if (loggedUser.isAdmin()) {
            ReservationRequest request = getRequestRepository().getById(id);
            return findForRequest(loggedUser, request);
        } else throw new AuthorizationException();
    }

    @Override
    public List<Room> findForRequest(User loggedUser, ReservationRequest request) {
        if (loggedUser.isAdmin())
            return getRoomRepository().findForRequest(request);
        else throw new AuthorizationException();
    }

    @Override
    public Room persist(User loggedUser, Room entity) {
        if (loggedUser.isAdmin())
            return getRoomRepository().persist(entity);
        else throw new AuthorizationException();
    }

    @Override
    public Room getById(User loggedUser, Integer id) {
        if (loggedUser.isAdmin())
            return getRoomRepository().getById(id);
        else throw new AuthorizationException();
    }

    @Override
    public boolean delete(User loggedUser, Integer id) {
        if (loggedUser.isAdmin())
            return getRoomRepository().delete(id);
        else throw new AuthorizationException();
    }

    @Override
    public Room update(User loggedUser, Integer id, Room entity) {
        if (loggedUser.isAdmin())
            return getRoomRepository().update(id, entity);
        else throw new AuthorizationException();
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
