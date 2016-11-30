package com.vaka.util;

import com.vaka.domain.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by Iaroslav on 11/26/2016.
 */
public class ServletDomainExtractor {
    private ServletDomainExtractor() {
    }

    public static ReservationRequest extractReservationRequest(HttpServletRequest req) {
        ReservationRequest reservation = new ReservationRequest();
        reservation.setCustomer(extractCustomer(req));
        String strId = req.getParameter("id");
        if (strId != null)
            reservation.setId(Integer.valueOf(strId));
        reservation.setNumOfBeds(Integer.valueOf(req.getParameter("numOfBeds")));
        reservation.setRoomClass(RoomClass.valueOf(req.getParameter("roomClazz")));
        reservation.setArrivalDate(LocalDate.parse(req.getParameter("arrivalDate")));
        reservation.setDepartureDate(LocalDate.parse(req.getParameter("departureDate")));
        reservation.setTotalCost(Integer.valueOf(req.getParameter("totalCost")));
        reservation.setCommentary(req.getParameter("commentary"));
        reservation.setStatus(ReservationRequestStatus.WAITING);
        return reservation;
    }

    public static Customer extractCustomer(HttpServletRequest req) {
        Customer customer = new Customer();
        customer.setEmail(req.getParameter("email"));
        customer.setPhoneNumber(req.getParameter("phoneNumber"));
        customer.setName(req.getParameter("name"));
        return customer;
    }

    //TODO move to other "factory" class
    public static Bill createFromReservation(Reservation reservation) {
        Bill bill = new Bill();
        bill.setOwner(reservation.getUser());
        bill.setReservation(reservation);
        bill.setRoom(reservation.getRoom());
        bill.setCreatedDate(LocalDateTime.now());
        return bill;
    }
}
