package com.vaka.web.controller;

import com.vaka.domain.Reservation;
import com.vaka.domain.User;
import com.vaka.service.CustomerService;
import com.vaka.service.ReservationService;
import com.vaka.service.RoomService;
import com.vaka.service.SecurityService;
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
    private CustomerService customerService;
    private RoomService roomService;
    private SecurityService securityService;


    public void create(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Reservation reservation = ServletDomainExtractor.extractReservation(req);
        User user = ServletDomainExtractor.extractCustomer(req);
        user = getCustomerService().createOrUpdate(user);
        getSecurityService().createToken(req, resp, user);
        reservation.setUser(user);
        reservation = getReservationService().create(reservation);
        resp.sendRedirect("/reservations/" + reservation.getId());
    }

    public void getById(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Integer reservationId = Integer.valueOf(req.getRequestURI().substring(req.getRequestURI().lastIndexOf('/') + 1));
            Reservation reservation = getReservationService().getById(reservationId);
            if (reservation == null) {
                resp.setStatus(400);
                return;
            }
            req.setAttribute("loggedUser", getSecurityService().authenticate(req, resp));
            req.setAttribute("reservation", reservation);
            req.getRequestDispatcher("/reservationInfo.jsp").forward(req, resp);
        } catch (NumberFormatException ex) {
            resp.setStatus(400);
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
        req.setAttribute("loggedUser", getSecurityService().authenticate(req, resp));
        req.setAttribute("reservationList", getReservationService().findConfirmed());
        req.getRequestDispatcher("/confirmedReservations.jsp").forward(req, resp);
    }

    public void requestsList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("loggedUser", getSecurityService().authenticate(req, resp));
        req.setAttribute("reservationList", getReservationService().findRequested());
        req.getRequestDispatcher("/reservationRequests.jsp").forward(req, resp);
    }

    public void requestProcess(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Integer reservationId = Integer.valueOf(req.getRequestURI().substring(req.getRequestURI().lastIndexOf('/') + 1));
            req.setAttribute("loggedUser", getSecurityService().authenticate(req, resp));
            req.setAttribute("reservation", getReservationService().getById(reservationId));
            req.setAttribute("rooms", getRoomService().findAvailableForReservation(reservationId));
            req.getRequestDispatcher("/reservationProcessing.jsp").forward(req, resp);
        } catch (NumberFormatException ex) {
            resp.setStatus(400);
        }
    }

    public CustomerService getCustomerService() {
        if (customerService == null) {
            synchronized (this) {
                if (customerService == null) {
                    customerService = ApplicationContext.getBean(CustomerService.class);
                }
            }
        }
        return customerService;
    }

    private SecurityService getSecurityService() {
        if (securityService == null) {
            synchronized (this) {
                if (securityService == null) {
                    securityService = ApplicationContext.getBean(SecurityService.class);
                }
            }
        }
        return securityService;
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
