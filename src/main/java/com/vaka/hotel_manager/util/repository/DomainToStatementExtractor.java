package com.vaka.hotel_manager.util.repository;

import com.vaka.hotel_manager.domain.Bill;
import com.vaka.hotel_manager.domain.Reservation;
import com.vaka.hotel_manager.domain.Room;
import com.vaka.hotel_manager.domain.User;
import com.vaka.hotel_manager.util.DateAndTimeUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Class that help extract entities fields to statement
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DomainToStatementExtractor {

    public static NamedPreparedStatement extract(User user, NamedPreparedStatement statement) throws SQLException {
        if (user.getId() != null)
            statement.setStatement("id", user.getId());
        if (user.getCreatedDatetime() != null)
            statement.setStatement("createdDatetime", Timestamp.valueOf(user.getCreatedDatetime()));
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
        statement.setStatement("createdDatetime", Timestamp.valueOf(bill.getCreatedDatetime()));
        statement.setStatement("totalCost", bill.getTotalCost());
        statement.setStatement("reservationId", bill.getReservation().getId());
        statement.setStatement("paid", bill.isPaid());
        return statement;
    }

    public static NamedPreparedStatement extract(Room room, NamedPreparedStatement statement) throws SQLException {
        if (room.getId() != null)
            statement.setStatement("id", room.getId());
        if (room.getCreatedDatetime() != null)
            statement.setStatement("createdDatetime", Timestamp.valueOf(room.getCreatedDatetime()));
        statement.setStatement("number", room.getNumber());
        statement.setStatement("capacity", room.getCapacity());
        statement.setStatement("costPerDay", room.getCostPerDay());
        statement.setStatement("roomClass", room.getRoomClazz().name());
        return statement;
    }

    public static NamedPreparedStatement extract(Reservation reservation, NamedPreparedStatement statement) throws SQLException {
        if (reservation.getId() != null)
            statement.setStatement("id", reservation.getId());
        if (reservation.getCreatedDatetime() != null)
            statement.setStatement("createdDatetime", Timestamp.valueOf(reservation.getCreatedDatetime()));
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
