package com.vaka.web.controller;

import com.vaka.domain.Reservation;
import com.vaka.service.ReservationService;
import com.vaka.service.RoomService;
import com.vaka.util.ApplicationContext;
import com.vaka.util.ServletDomainExtractor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Iaroslav on 12/1/2016.
 */
public class ReservationController {
    private ReservationService reservationService;
    private RoomService roomService;


    public void create(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        Reservation reservation = ServletDomainExtractor.extractReservation(req);
        reservation = getReservationService().create(reservation);
        response.sendRedirect("/reservations/" + reservation.getId());
    }

    public void getById(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        try {
            Integer reservationId = Integer.valueOf(req.getRequestURI().substring(req.getRequestURI().lastIndexOf('/') + 1));
            Reservation reservation = getReservationService().getById(reservationId);
            if (reservation == null) {
                response.setStatus(400);
                return;
            }
            req.setAttribute("reservation", reservation);
            req.getRequestDispatcher("/reservationInfo.jsp").forward(req, response);
        } catch (NumberFormatException ex) {
            response.setStatus(400);
        }
    }

    public void applyRoomForReservation(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        Integer requestId = Integer.valueOf(req.getParameter("reqId"));
        Integer roomId = Integer.valueOf(req.getParameter("roomId"));
        if (requestId == null || roomId == null || requestId < 0 || roomId < 0) {
            response.setStatus(400);
            return;
        }
        Reservation reservation = getReservationService().applyRoomForReservation(roomId, requestId);
        response.sendRedirect("/reservations/" + reservation.getId());
    }

    public void confirmedList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("reservationList", getReservationService().findConfirmed());
        req.getRequestDispatcher("/confirmedReservations.jsp").forward(req, resp);
    }

    public void requestsList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("reservationList", getReservationService().findRequested());
        req.getRequestDispatcher("/reservationRequests.jsp").forward(req, resp);
    }

    public void requestProcess(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Integer reservationId = Integer.valueOf(req.getRequestURI().substring(req.getRequestURI().lastIndexOf('/') + 1));
            req.setAttribute("reservation", getReservationService().getById(reservationId));
            req.setAttribute("rooms", getRoomService().findAvailableForReservation(reservationId));
            req.getRequestDispatcher("/reservationProcessing.jsp").forward(req, resp);
        } catch (NumberFormatException ex) {
            resp.setStatus(400);
        }
    }

    private ReservationService getReservationService() {
        if (reservationService == null) {
            synchronized (this) {
                if (reservationService == null) {
                    reservationService = ApplicationContext.getBean(ReservationService.class);
                }
            }
        }
        return reservationService;
    }

    public RoomService getRoomService() {
        if (roomService == null) {
            synchronized (this) {
                if (roomService == null) {
                    roomService = ApplicationContext.getBean(RoomService.class);
                }
            }
        }
        return roomService;
    }
}
