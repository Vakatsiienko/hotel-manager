package com.vaka.web.controller;

import com.vaka.domain.ReservationRequest;
import com.vaka.domain.Room;
import com.vaka.domain.User;
import com.vaka.service.RoomService;
import com.vaka.util.ApplicationContext;

import java.util.List;

/**
 * Created by Iaroslav on 11/25/2016.
 */
public class RoomController {
    private RoomService roomService;

    public List<Room> getForRequestById(User loggedUser, Integer reqId) {
        return getRoomService().getForRequestId(loggedUser, reqId);
    }
    public List<Room> getForRequest(User loggedUser, ReservationRequest request) {
        return getRoomService().getForRequest(loggedUser, request);
    }


    public RoomService getRoomService() {
        if (roomService == null)
            synchronized (this){
                if (roomService == null) {
                    roomService = ApplicationContext.getBean(RoomService.class);
                }
            }
        return roomService;
    }
}
