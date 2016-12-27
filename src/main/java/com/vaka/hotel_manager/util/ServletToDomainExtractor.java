package com.vaka.hotel_manager.util;

import com.vaka.hotel_manager.domain.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Same as util extractors, only with Servlet.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServletToDomainExtractor {
    private static final Logger LOG = LoggerFactory.getLogger(ServletToDomainExtractor.class);
    public static Reservation extractReservation(HttpServletRequest req) {
        try {
            Reservation reservation = new Reservation();
            String strId = req.getParameter("id");
            if (strId != null)
                reservation.setId(Integer.valueOf(strId));
            reservation.setGuests(Integer.valueOf(req.getParameter("guests")));
            String[] roomClass = req.getParameter("roomClass").toUpperCase().split(" ");
            reservation.setRequestedRoomClass(RoomClass.valueOf(String.join("_", roomClass)));
            reservation.setArrivalDate(LocalDate.parse(req.getParameter("arrivalDate")));
            reservation.setDepartureDate(LocalDate.parse(req.getParameter("departureDate")));
            reservation.setStatus(ReservationStatus.REQUESTED);
            return reservation;
        } catch (DateTimeParseException | NumberFormatException e) {
            LOG.debug(e.getMessage(), e);
            throw new IllegalArgumentException(e.getMessage());
        }
    }
    public static User extractCustomer(HttpServletRequest req) {
        User user = new User();
        user.setEmail(req.getParameter("email"));
        user.setPhoneNumber(req.getParameter("phoneNumber"));
        user.setName(req.getParameter("name"));
        user.setPassword(req.getParameter("password"));
        user.setRole(Role.CUSTOMER);
        return user;
    }

    public static Room extractRoom(HttpServletRequest req) {
        try {
            Room room = new Room();
            room.setCapacity(Integer.valueOf(req.getParameter("capacity")));
            room.setCostPerDay(Integer.valueOf(req.getParameter("costPerDay")));
            room.setRoomClazz(RoomClass.valueOf(req.getParameter("roomClass")));
            room.setNumber(Integer.valueOf(req.getParameter("number")));
            return room;
        } catch (NumberFormatException e) {
            LOG.debug(e.getMessage(), e);
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
