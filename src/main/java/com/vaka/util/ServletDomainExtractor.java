package com.vaka.util;

import com.vaka.domain.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

/**
 * Created by Iaroslav on 11/26/2016.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServletDomainExtractor {

    public static Reservation extractReservation(HttpServletRequest req) {
        Reservation reservation = new Reservation();
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
        user.setRole(Role.CUSTOMER);
        return user;
    }

    //TODO move to other "factory" class
    public static Bill createFromReservation(Reservation reservation) {
        Bill bill = new Bill();
        bill.setReservation(reservation);
        bill.setTotalCost((int) (reservation.getRoom().getCostPerDay() * (reservation.getDepartureDate().toEpochDay() - reservation.getArrivalDate().toEpochDay())));
        return bill;
    }
}
