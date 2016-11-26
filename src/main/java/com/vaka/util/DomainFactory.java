package com.vaka.util;

import com.vaka.domain.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

/**
 * Created by Iaroslav on 11/26/2016.
 */
public class DomainFactory {
    private DomainFactory(){}

    public static ReservationRequest createFromRequest(HttpServletRequest req, User loggedUser){
        ReservationRequest reservation = new ReservationRequest();
        reservation.setCustomer(loggedUser);
        reservation.setId(Integer.valueOf(req.getParameter("id")));
        reservation.setNumOfBeds(Integer.valueOf(req.getParameter("numOfBeds")));
        reservation.setTotalCost(Integer.valueOf(req.getParameter("totalCost")));
        reservation.setRoomClass(RoomClass.valueOf(req.getParameter("roomClass")));
        reservation.setBathroomType(BathroomType.valueOf(req.getParameter("bathroomType")));
        reservation.setStartDate(LocalDate.parse(req.getParameter("startDate")));
        reservation.setEndDate(LocalDate.parse(req.getParameter("endDate")));
        reservation.setStatus(ReservationRequestStatus.WAITING);
        reservation.setCommentary(req.getParameter("commentary"));
        return reservation;
    }
}
