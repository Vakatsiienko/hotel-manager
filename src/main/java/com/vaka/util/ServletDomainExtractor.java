package com.vaka.util;

import com.vaka.domain.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

/**
 * Created by Iaroslav on 11/26/2016.
 */
public class ServletDomainExtractor {
    private ServletDomainExtractor() {
    }

    public static Reservation extractReservation(HttpServletRequest req) {
        Reservation reservation = new Reservation();
        reservation.setUser(extractCustomer(req));
        String strId = req.getParameter("id");
        if (strId != null)
            reservation.setId(Integer.valueOf(strId));
        reservation.setGuests(Integer.valueOf(req.getParameter("guests")));
        reservation.setRequestedRoomClass(RoomClass.valueOf(req.getParameter("roomClazz")));
        reservation.setArrivalDate(LocalDate.parse(req.getParameter("arrivalDate")));
        reservation.setDepartureDate(LocalDate.parse(req.getParameter("departureDate")));
        reservation.setStatus(ReservationStatus.REQUESTED);
        return reservation;
    }

    public static User extractCustomer(HttpServletRequest req) {
        User user = new User();
        user.setEmail(req.getParameter("email"));
        user.setPhoneNumber(req.getParameter("phoneNumber"));
        user.setName(req.getParameter("name"));
        return user;
    }

    //TODO move to other "factory" class
    public static Bill createFromReservation(Reservation reservation) {
        Bill bill = new Bill();
        bill.setOwner(reservation.getUser());
        bill.setReservation(reservation);
        return bill;
    }
}
