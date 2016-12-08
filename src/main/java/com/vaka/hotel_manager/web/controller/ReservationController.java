package com.vaka.hotel_manager.web.controller;

import com.vaka.hotel_manager.context.ApplicationContext;
import com.vaka.hotel_manager.domain.Reservation;
import com.vaka.hotel_manager.domain.ReservationStatus;
import com.vaka.hotel_manager.domain.Role;
import com.vaka.hotel_manager.domain.User;
import com.vaka.hotel_manager.service.ReservationService;
import com.vaka.hotel_manager.service.RoomService;
import com.vaka.hotel_manager.service.SecurityService;
import com.vaka.hotel_manager.service.UserService;
import com.vaka.hotel_manager.util.DomainExtractor;
import com.vaka.hotel_manager.util.DomainUtil;
import com.vaka.hotel_manager.util.exception.AuthorizationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by Iaroslav on 12/1/2016.
 */
//TODO make private default constructors
public class ReservationController {
    private ReservationService reservationService;
    private UserService userService;
    private RoomService roomService;
    private SecurityService securityService;

//TODO don't give resp to controllers
    public void create(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User loggedUser = getSecurityService().authenticate(req, resp);
        Reservation reservation = DomainExtractor.extractReservation(req);
        if (DomainUtil.hasNull(reservation) ||
                reservation.getArrivalDate().compareTo(reservation.getDepartureDate()) >= 0) {
            resp.sendError(400, "Illegal arguments");
            return;
        }
        if (loggedUser.getRole() == Role.ANONYMOUS) {
            User created = DomainExtractor.extractUser(req);
            created.setPassword(created.getPhoneNumber());
            if (DomainUtil.hasNull(created)) {
                resp.sendError(400, "Illegal arguments");
                return;
            }
            created = getUserService().create(loggedUser, created);
            reservation.setUser(created);
            getSecurityService().signIn(req, resp, created.getEmail(), created.getPhoneNumber());
        } else {
            reservation.setUser(loggedUser);
        }
        reservation = getReservationService().create(loggedUser, reservation);
        resp.sendRedirect("/reservations/" + reservation.getId());
    }

    public void getById(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            User loggedUser = getSecurityService().authenticate(req, resp);
            Integer reservationId = Integer.valueOf(req.getRequestURI().substring(req.getRequestURI().lastIndexOf('/') + 1));
            Optional<Reservation> reservation = getReservationService().getById(loggedUser, reservationId);
            if (!reservation.isPresent()) {
                resp.sendError(404);
                return;
            }
            req.setAttribute("loggedUser", loggedUser);
            req.setAttribute("reservation", reservation.get());
            req.getRequestDispatcher("/reservationInfo.jsp").forward(req, resp);
        } catch (NumberFormatException ex) {
            resp.sendError(400, "Illegal arguments");
        }
    }

    public void applyRoomForReservation(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User loggedUser = getSecurityService().authenticate(req, resp);
        if (loggedUser.getRole() != Role.MANAGER)
            throw new AuthorizationException("Not allowed.");//TODO move to service

        Integer reservationId = -1;
        Integer roomId = -1;
        try {
            reservationId = Integer.valueOf(req.getParameter("reservationId"));
            roomId = Integer.valueOf(req.getParameter("roomId"));
            if (reservationId < 0 || roomId < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            resp.sendError(400, "Illegal arguments");
            return;
        }
        Optional<Reservation> reservation = getReservationService().getById(loggedUser, reservationId);
        boolean success = false;
        if (reservation.isPresent()) {
            success = getReservationService().applyRoomForReservation(loggedUser, roomId, reservationId);
            req.setAttribute("reservation", getReservationService().getById(loggedUser, reservationId).get());//TODO CHECK
        }
        if (!success) {
            req.setAttribute("exception", "Can't apply given room");
            req.getRequestDispatcher("/reservationProcessing.jsp").forward(req, resp);
        }
        resp.sendRedirect("/reservations/" + reservationId);
    }//TODO move url location to another prop file

    public void confirmedList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User loggedUser = getSecurityService().authenticate(req, resp);

        req.setAttribute("reservationList", getReservationService().findByStatus(loggedUser, ReservationStatus.CONFIRMED));
        req.setAttribute("loggedUser", loggedUser);
        req.getRequestDispatcher("/confirmedReservations.jsp").forward(req, resp);
    }

    public void requestsList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User loggedUser = getSecurityService().authenticate(req, resp);
        req.setAttribute("reservationList", getReservationService().findByStatus(loggedUser, ReservationStatus.REQUESTED));
        req.setAttribute("loggedUser", loggedUser);
        req.getRequestDispatcher("/reservationRequests.jsp").forward(req, resp);
    }

    public void requestProcess(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            User loggedUser = getSecurityService().authenticate(req, resp);
            Integer reservationId = Integer.valueOf(req.getRequestURI().substring(req.getRequestURI().lastIndexOf('/') + 1));
            Optional<Reservation> reservation = getReservationService().getById(loggedUser, reservationId);
            if (!reservation.isPresent()) {
                resp.sendError(404);
                return;
            }
            req.setAttribute("loggedUser", loggedUser);
            req.setAttribute("reservation", reservation.get());
            req.setAttribute("rooms", getRoomService().findAvailableForReservation(loggedUser, reservationId));
            req.getRequestDispatcher("/reservationProcessing.jsp").forward(req, resp);
        } catch (NumberFormatException ex) {
            resp.sendError(400);
        }
    }

    public void getByUser(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        User loggedUser = getSecurityService().authenticate(req, resp);
        if (loggedUser.getRole() == Role.ANONYMOUS)
            resp.sendRedirect("/signin");
        List<Reservation> requestedList = getReservationService().findByStatusAndUserId(loggedUser, ReservationStatus.REQUESTED, loggedUser.getId());
        List<Reservation> confirmedList = getReservationService().findByStatusAndUserId(loggedUser, ReservationStatus.CONFIRMED, loggedUser.getId());
        List<Reservation> rejectedList = getReservationService().findByStatusAndUserId(loggedUser, ReservationStatus.REJECTED, loggedUser.getId());
        req.setAttribute("requestedList", requestedList);
        req.setAttribute("confirmedList", confirmedList);
        req.setAttribute("rejectedList", rejectedList);
        req.getRequestDispatcher("/userReservations.jsp").forward(req, resp);
    }

    public void reject(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User loggedUser = getSecurityService().authenticate(req, resp);
        Integer reservationId = Integer.valueOf(req.getParameter("reservationId"));
        getReservationService().reject(loggedUser, reservationId);
        resp.sendRedirect("/reservations/" + reservationId);
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
