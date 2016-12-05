package com.vaka.web.controller;

import com.vaka.domain.Reservation;
import com.vaka.domain.ReservationStatus;
import com.vaka.domain.User;
import com.vaka.service.UserService;
import com.vaka.service.ReservationService;
import com.vaka.service.RoomService;
import com.vaka.service.SecurityService;
import com.vaka.context.ApplicationContext;
import com.vaka.util.DomainExtractor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * Created by Iaroslav on 12/1/2016.
 */
public class ReservationController {
    private ReservationService reservationService;
    private UserService userService;
    private RoomService roomService;
    private SecurityService securityService;


    public void create(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User loggedUser = getSecurityService().authenticate(req, resp);
        Reservation reservation = DomainExtractor.extractReservation(req);
        User user = DomainExtractor.extractUser(req);
        user = getUserService().createOrUpdate(loggedUser, user);
        getSecurityService().signIn(req, resp, user.getEmail(), user.getPassword());
        reservation.setUser(user);
        reservation = getReservationService().create(loggedUser, reservation);
        resp.sendRedirect("/reservations/" + reservation.getId());
    }

    public void getById(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            User loggedUser = getSecurityService().authenticate(req, resp);
            Integer reservationId = Integer.valueOf(req.getRequestURI().substring(req.getRequestURI().lastIndexOf('/') + 1));
            Optional<Reservation> reservation = getReservationService().getById(loggedUser, reservationId);
            if (!reservation.isPresent()) {
                resp.setStatus(404);
                return;
            }
            req.setAttribute("loggedUser", loggedUser);
            req.setAttribute("reservation", reservation.get());
            req.getRequestDispatcher("/reservationInfo.jsp").forward(req, resp);
        } catch (NumberFormatException ex) {
            resp.setStatus(400);
        }
    }

    public void applyRoomForReservation(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User loggedUser = getSecurityService().authenticate(req, resp);
        Integer requestId = Integer.valueOf(req.getParameter("reqId"));
        Integer roomId = Integer.valueOf(req.getParameter("roomId"));
        if (requestId == null || roomId == null || requestId < 0 || roomId < 0) {
            resp.setStatus(400);
            return;
        }
        Reservation reservation = getReservationService().applyRoomForReservation(loggedUser, roomId, requestId);
        resp.sendRedirect("/reservations/" + reservation.getId());
    }

    public void confirmedList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User loggedUser = getSecurityService().authenticate(req, resp);

        req.setAttribute("loggedUser", getSecurityService().authenticate(req, resp));
        req.setAttribute("reservationList", getReservationService().findByStatus(loggedUser, ReservationStatus.CONFIRMED));
        req.getRequestDispatcher("/confirmedReservations.jsp").forward(req, resp);
    }

    public void requestsList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User loggedUser = getSecurityService().authenticate(req, resp);
        req.setAttribute("loggedUser", loggedUser);
        req.setAttribute("reservationList", getReservationService().findByStatus(loggedUser, ReservationStatus.REQUESTED));
        req.getRequestDispatcher("/reservationRequests.jsp").forward(req, resp);
    }

    public void requestProcess(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            User loggedUser = getSecurityService().authenticate(req, resp);
            Integer reservationId = Integer.valueOf(req.getRequestURI().substring(req.getRequestURI().lastIndexOf('/') + 1));
            Optional<Reservation> reservation = getReservationService().getById(loggedUser, reservationId);
            if (!reservation.isPresent()) {
                resp.setStatus(404);
                return;
            }
            req.setAttribute("loggedUser", loggedUser);
            req.setAttribute("reservation", getReservationService().getById(loggedUser, reservationId));
            req.setAttribute("rooms", getRoomService().findAvailableForReservation(loggedUser, reservationId));
            req.getRequestDispatcher("/reservationProcessing.jsp").forward(req, resp);
        } catch (NumberFormatException ex) {
            resp.setStatus(400);
        }
    }

    public UserService getUserService() {
        if (userService == null) {
            synchronized (this) {
                if (userService == null) {
                    userService = ApplicationContext.getInstance().getBean(UserService.class);
                }
            }
        }
        return userService;
    }

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
    private ReservationService getReservationService() {
        if (reservationService == null) {
            synchronized (this) {
                if (reservationService == null) {
                    reservationService = ApplicationContext.getInstance().getBean(ReservationService.class);
                }
            }
        }
        return reservationService;
    }

    public RoomService getRoomService() {
        if (roomService == null) {
            synchronized (this) {
                if (roomService == null) {
                    roomService = ApplicationContext.getInstance().getBean(RoomService.class);
                }
            }
        }
        return roomService;
    }
}
