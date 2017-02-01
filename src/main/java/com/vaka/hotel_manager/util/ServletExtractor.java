package com.vaka.hotel_manager.util;

import com.vaka.hotel_manager.domain.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.function.Function;

/**
 * Same as util extractors, only with Servlet.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServletExtractor {
    private static final Logger LOG = LoggerFactory.getLogger(ServletExtractor.class);

    public static <T> T extractOrDefault(String paramValue, T defaultValue, Function<String, T> parser) {
        Optional<String> defaultOpt = Optional.ofNullable(paramValue);
        if (defaultOpt.isPresent())
            return parser.apply(defaultOpt.get());
        else return defaultValue;
    }

    public static Reservation extractReservation(HttpServletRequest req) {
        try {
            Reservation reservation = new Reservation();
            String strId = req.getParameter("id");
            if (strId != null)
                reservation.setId(Integer.valueOf(strId));
            reservation.setGuests(Integer.valueOf(req.getParameter("guests")));
            reservation.setRequestedRoomClass(extractRoomClass(req));
            reservation.setArrivalDate(LocalDate.parse(req.getParameter("arrivalDate")));
            reservation.setDepartureDate(LocalDate.parse(req.getParameter("departureDate")));
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
            room.setRoomClass(extractRoomClass(req));
            room.setNumber(Integer.valueOf(req.getParameter("number")));
            return room;
        } catch (NumberFormatException e) {
            LOG.debug(e.getMessage(), e);
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public static RoomClass extractRoomClass(HttpServletRequest req) {
        String strId = req.getParameter("roomClassId");
        String name = req.getParameter("roomClassName");
        RoomClass roomClass = new RoomClass();
        roomClass.setName(name);
        if (strId != null) {
            try {
                roomClass.setId(Integer.parseInt(strId));
            } catch (NumberFormatException e) {
                LOG.info(e.getMessage(), e);
                throw new IllegalArgumentException(e.getMessage());
            }
        }
        return roomClass;
    }
}
