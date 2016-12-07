package com.vaka.util.repository;

import com.vaka.domain.Bill;
import com.vaka.domain.Reservation;
import com.vaka.domain.Room;
import com.vaka.domain.User;
import com.vaka.util.DateAndTimeUtil;
import com.vaka.util.repository.NamedPreparedStatement;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Created by Iaroslav on 12/3/2016.
 */
public class StatementExtractor {

    public static NamedPreparedStatement extract(User user, NamedPreparedStatement statement) throws SQLException {
        if (user.getId() != null)
            statement.setStatement("id", user.getId());
        statement.setStatement("createdDatetime", DateAndTimeUtil.convertWithoutMilli(user.getCreatedDatetime()));
        statement.setStatement("email", user.getEmail());
        statement.setStatement("password", user.getPassword());
        statement.setStatement("name", user.getName());
        statement.setStatement("role", user.getRole().name());
        statement.setStatement("phoneNumber", user.getPhoneNumber());
        return statement;
    }

    public static NamedPreparedStatement extract(Bill bill, NamedPreparedStatement statement) throws SQLException {
        if (bill.getId() != null)
            statement.setStatement("id", bill.getId());
        statement.setStatement("createdDatetime", DateAndTimeUtil.convertWithoutMilli(bill.getCreatedDatetime()));
        statement.setStatement("totalCost", bill.getTotalCost());
        statement.setStatement("reservationId", bill.getReservation().getId());
        statement.setStatement("paid", bill.isPaid());
        return statement;
    }

    public static NamedPreparedStatement extract(Room room, NamedPreparedStatement statement) throws SQLException {
        if (room.getId() != null)
            statement.setStatement("id", room.getId());
        statement.setStatement("createdDatetime", DateAndTimeUtil.convertWithoutMilli(room.getCreatedDatetime()));
        statement.setStatement("number", room.getNumber());
        statement.setStatement("capacity", room.getCapacity());
        statement.setStatement("costPerDay", room.getCostPerDay());
        statement.setStatement("roomClass", room.getRoomClazz().name());
        statement.setStatement("description", room.getDescription());
        return statement;
    }

    public static NamedPreparedStatement extract(Reservation reservation, NamedPreparedStatement statement) throws SQLException {
        if (reservation.getId() != null)
            statement.setStatement("id", reservation.getId());
        statement.setStatement("createdDatetime", DateAndTimeUtil.convertWithoutMilli(reservation.getCreatedDatetime()));
        statement.setStatement("userId", reservation.getUser().getId());
        if (reservation.getRoom() != null)
            statement.setStatement("roomId", reservation.getRoom().getId());
        statement.setStatement("guests", reservation.getGuests());
        statement.setStatement("requestedRoomClass", reservation.getRequestedRoomClass().name());
        statement.setStatement("status", reservation.getStatus().name());
        statement.setStatement("arrivalDate", Date.valueOf(reservation.getArrivalDate()));
        statement.setStatement("departureDate", Date.valueOf(reservation.getDepartureDate()));
        return statement;
    }
}
