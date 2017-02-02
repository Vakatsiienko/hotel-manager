package com.vaka.hotel_manager.web.controller;

import com.vaka.hotel_manager.core.context.ApplicationContext;
import com.vaka.hotel_manager.core.security.SecurityService;
import com.vaka.hotel_manager.domain.entities.Reservation;
import com.vaka.hotel_manager.domain.ReservationStatus;
import com.vaka.hotel_manager.domain.Role;
import com.vaka.hotel_manager.domain.entities.User;
import com.vaka.hotel_manager.service.ReservationService;
import com.vaka.hotel_manager.service.RoomClassService;
import com.vaka.hotel_manager.service.RoomService;
import com.vaka.hotel_manager.service.UserService;
import com.vaka.hotel_manager.util.ServletExtractor;
import com.vaka.hotel_manager.util.ValidationUtil;
import com.vaka.hotel_manager.util.exception.CreatingException;
import com.vaka.hotel_manager.util.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Created by Iaroslav on 12/1/2016.
 */
public class ReservationController {
    private static final Logger LOG = LoggerFactory.getLogger(ReservationController.class);
    private ReservationService reservationService;
    private UserService userService;
    private RoomService roomService;
    private RoomClassService roomClassService;

    private SecurityService securityService;

    /**
     * Creating reservation and if user isn't authenticated - trying to register by given info
     */
    public void create(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User loggedUser = getSecurityService().authenticate(req.getSession());
        LOG.debug("Creating reservation request");
        Reservation reservation = ServletExtractor.extractReservation(req);
        ValidationUtil.validate(reservation);
        if (loggedUser.getRole() == Role.ANONYMOUS) {
            LOG.debug("Creating user for reservation");
            User created = ServletExtractor.extractCustomer(req);
            created.setPassword(created.getPhoneNumber());
            ValidationUtil.validate(created);
            try {
                created = getUserService().create(loggedUser, created);
            } catch (CreatingException e){
                LOG.debug(e.getMessage(), e);
                req.getSession().setAttribute("exception", e.getMessage());
                req.getSession().setAttribute("email", created.getEmail());
                req.getSession().setAttribute("reservation", reservation);
                resp.sendRedirect("/signin?redirectUri=/");
                return;
            }
            reservation.setUser(created);
            getSecurityService().signIn(req.getSession(), created.getEmail(), created.getPhoneNumber());
        } else {
            reservation.setUser(loggedUser);
        }
        reservation = getReservationService().create(loggedUser, reservation);
        req.getSession().removeAttribute("reservation");
        LOG.debug("To reservation page, reservationId: {}", reservation.getId());
        resp.sendRedirect("/reservations/" + reservation.getId());
    }


    public void getById(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User loggedUser = getSecurityService().authenticate(req.getSession());
        LOG.debug("To reservation page");
        try {
            Integer reservationId = Integer.valueOf(req.getRequestURI().substring(req.getRequestURI().lastIndexOf('/') + 1));
            Optional<Reservation> reservation = getReservationService().getById(loggedUser, reservationId);
            if (!reservation.isPresent()) {
                throw new NotFoundException("There are no reservation with given ID");
            }
            if (reservation.get().getStatus() == ReservationStatus.REQUESTED)
                req.setAttribute("availableRooms", getRoomService().findAvailableForReservation(loggedUser, reservation.get().getId()));
            req.setAttribute("reservation", reservation.get());
            req.getRequestDispatcher("/jsp/reservationInfo.jsp").forward(req, resp);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("ID can be only integer value");
        }
    }

    public void applyRoomForReservation(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User loggedUser = getSecurityService().authenticate(req.getSession());
        LOG.debug("Trying to apply room for reservation request");
        Integer reservationId;
        Integer roomId;
        try {
            reservationId = Integer.valueOf(req.getParameter("reservationId"));
            roomId = Integer.valueOf(req.getParameter("roomId"));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID can be only integer value");
        }
        String exceptionMessage = "Given room doesn't fit ";
        boolean success = false;
        try {
            success = getReservationService().applyRoom(loggedUser, roomId, reservationId);
        } catch (IllegalStateException ex) {
            LOG.debug(ex.getMessage(), ex);
            exceptionMessage = ex.getMessage();
        }
        if (!success) {
            req.setAttribute("reservation", getReservationService().getById(loggedUser, reservationId).get());
            req.setAttribute("rooms", getRoomService().findAvailableForReservation(loggedUser, reservationId));
            req.setAttribute("exception", exceptionMessage);
            req.getRequestDispatcher("/jsp/reservationInfo.jsp").forward(req, resp);
        }
        LOG.debug("Redirect to reservation page, reservationId: {}", reservationId);
        resp.sendRedirect("/reservations/" + reservationId);
    }

    public void confirmedPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User loggedUser = getSecurityService().authenticate(req.getSession());
        LOG.debug("To confirmed reservations page");
        Integer page = ServletExtractor.extractOrDefault(req.getParameter("page"), 1, Integer::parseInt);
        Integer size = ServletExtractor.extractOrDefault(req.getParameter("size"), 10, Integer::parseInt);
        req.setAttribute("reservationPage", getReservationService().findPageByStatusFromDate(loggedUser, ReservationStatus.CONFIRMED, LocalDate.now(), page, size));
        req.setAttribute("roomClasses", getRoomClassService().findAll(loggedUser));
        req.getRequestDispatcher("/jsp/confirmedReservations.jsp").forward(req, resp);
    }

    public void showArrival(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User loggedUser = getSecurityService().authenticate(req.getSession());
        LOG.debug("Show arrival reservations");
        Integer page = ServletExtractor.extractOrDefault(req.getParameter("page"), 1, Integer::parseInt);
        Integer size = ServletExtractor.extractOrDefault(req.getParameter("size"), 10, Integer::parseInt);
        String roomClassName = ServletExtractor.getOrDefault(req.getParameter("roomClass"), "Any");
        LocalDate arrivalDate = ServletExtractor.extractOrDefault(req.getParameter("arrivalDate"), LocalDate.now(), LocalDate::parse);
        req.setAttribute("reservationPage", getReservationService().findPageActiveByRoomClassNameAndArrivalDate(loggedUser, roomClassName, arrivalDate, page, size));
        req.setAttribute("roomClasses", getRoomClassService().findAll(loggedUser));
        req.getRequestDispatcher("/jsp/confirmedReservations.jsp").forward(req, resp);
    }

    public void requestsList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User loggedUser = getSecurityService().authenticate(req.getSession());
        LOG.debug("To requested reservations page");
        Integer page = ServletExtractor.extractOrDefault(req.getParameter("page"), 1, Integer::parseInt);
        Integer size = ServletExtractor.extractOrDefault(req.getParameter("size"), 10, Integer::parseInt);
        req.setAttribute("reservationPage", getReservationService().findPageByStatusFromDate(loggedUser, ReservationStatus.REQUESTED, LocalDate.MIN, page, size));
        req.getRequestDispatcher("/jsp/reservationRequests.jsp").forward(req, resp);
    }

    /**
     * Finding all available rooms for given reservation
     */
    public void processingPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User loggedUser = getSecurityService().authenticate(req.getSession());
        LOG.debug("Processing reservation request page");
        Integer reservationId = Integer.valueOf(req.getRequestURI().substring(req.getRequestURI().lastIndexOf('/') + 1));
        Optional<Reservation> reservation = getReservationService().getById(loggedUser, reservationId);
        if (!reservation.isPresent()) {
            throw new NotFoundException("Reservation not found");
        }
        req.setAttribute("reservation", reservation.get());
        req.setAttribute("rooms", getRoomService().findAvailableForReservation(loggedUser, reservationId));
        req.getRequestDispatcher("/jsp/reservationProcessing.jsp").forward(req, resp);
    }

    public void reject(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User loggedUser = getSecurityService().authenticate(req.getSession());
        LOG.debug("Reservation reject request");
        Integer reservationId = Integer.valueOf(req.getRequestURI().split("/")[2]);
        getReservationService().reject(loggedUser, reservationId);
        LOG.debug("Redirecting to reservation page, reservationId: {}", reservationId);
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

    public SecurityService getSecurityService() {
        if (securityService == null) {
            synchronized (this) {
                if (securityService == null) {
                    securityService = ApplicationContext.getInstance().getBean(SecurityService.class);
                }
            }
        }
        return securityService;
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

    public RoomClassService getRoomClassService() {
        if (roomClassService == null) {
            synchronized (this) {
                if (roomClassService == null) {
                    roomClassService = ApplicationContext.getInstance().getBean(RoomClassService.class);
                }
            }
        }
        return roomClassService;
    }
}
