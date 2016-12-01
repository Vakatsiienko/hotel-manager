package com.vaka.web.controller;

import com.vaka.service.ReservationService;
import com.vaka.service.RoomService;
import com.vaka.util.ApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Iaroslav on 11/25/2016.
 */
public class RoomController {
    private RoomService roomService;
    private ReservationService reservationService;
    

    public RoomService getRoomService() {
        if (roomService == null)
            synchronized (this){
                if (roomService == null) {
                    roomService = ApplicationContext.getBean(RoomService.class);
                }
            }
        return roomService;
    }

    public ReservationService getReservationService() {
        if (reservationService == null) {
            synchronized (this) {
                if (reservationService == null) {
                    reservationService = ApplicationContext.getBean(ReservationService.class);
                }
            }
        }
        return reservationService;
    }
}
