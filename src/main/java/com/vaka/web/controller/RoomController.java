package com.vaka.web.controller;

import com.vaka.service.ReservationService;
import com.vaka.service.RoomService;
import com.vaka.service.SecurityService;
import com.vaka.context.ApplicationContext;

/**
 * Created by Iaroslav on 11/25/2016.
 */
public class RoomController {
    private RoomService roomService;
    private ReservationService reservationService;
    private SecurityService securityService;

    private SecurityService getSecurityService() {
        if (securityService == null) {
            synchronized (this) {
                if (securityService == null) {
                    securityService = ApplicationContext.getInstance().getBean(SecurityService.class);
                }
            }
        }
        return securityService;
    }

    public RoomService getRoomService() {
        if (roomService == null)
            synchronized (this){
                if (roomService == null) {
                    roomService = ApplicationContext.getInstance().getBean(RoomService.class);
                }
            }
        return roomService;
    }

    public ReservationService getReservationService() {
        if (reservationService == null) {
            synchronized (this) {
                if (reservationService == null) {
                    reservationService = ApplicationContext.getInstance().getBean(ReservationService.class);
                }
            }
        }
        return reservationService;
    }
}
