package com.vaka.hotel_manager.util;

import com.vaka.hotel_manager.domain.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

/**
 * Created by Iaroslav on 12/9/2016.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServletToDomainExtractor {
    //TODO change apply to confirm on reservation page
    public static Reservation extractReservation(HttpServletRequest req) {
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
    }
    //TODO wrap request
    public static User extractUser(HttpServletRequest req) {
        User user = new User();
        user.setEmail(req.getParameter("email"));
        user.setPhoneNumber(req.getParameter("phoneNumber"));
        user.setName(req.getParameter("name"));
        user.setPassword(req.getParameter("password"));
        user.setRole(Role.CUSTOMER);
        return user;
    }
}
