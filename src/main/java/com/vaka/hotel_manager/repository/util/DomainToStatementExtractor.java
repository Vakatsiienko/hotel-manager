package com.vaka.hotel_manager.repository.util;

import com.vaka.hotel_manager.domain.*;
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
            statement.setStatement("userId", user.getId());
        if (user.getCreatedDatetime() != null)
            statement.setStatement("userCreatedDatetime", Timestamp.valueOf(user.getCreatedDatetime()));
        statement.setStatement("userEmail", user.getEmail());
        statement.setStatement("userPassword", user.getPassword());
        statement.setStatement("userName", user.getName());
        statement.setStatement("userRole", user.getRole().name());
        statement.setStatement("userPhoneNumber", user.getPhoneNumber());
        return statement;
    }

    public static NamedPreparedStatement extract(RoomClass roomClass, NamedPreparedStatement statement) throws SQLException {
        if (roomClass.getId() != null) {
            statement.setStatement("roomClassId", roomClass.getId());
            statement.setStatement("roomClassId", roomClass.getId());
        }
        if (roomClass.getCreatedDatetime() != null)
            statement.setStatement("roomClassCreatedDatetime", Timestamp.valueOf(roomClass.getCreatedDatetime()));
        statement.setStatement("roomClassName", roomClass.getName());
        return statement;
    }

    public static NamedPreparedStatement extract(Bill bill, NamedPreparedStatement statement) throws SQLException {
        if (bill.getId() != null)
            statement.setStatement("billId", bill.getId());
        statement.setStatement("billCreatedDatetime", Timestamp.valueOf(bill.getCreatedDatetime()));
        statement.setStatement("billTotalCost", bill.getTotalCost());
        statement.setStatement("billReservationId", bill.getReservation().getId());
        statement.setStatement("billPaid", bill.isPaid());
        return statement;
    }

    public static NamedPreparedStatement extract(Room room, NamedPreparedStatement statement) throws SQLException {
        if (room.getId() != null)
            statement.setStatement("roomId", room.getId());
        if (room.getCreatedDatetime() != null)
            statement.setStatement("roomCreatedDatetime", Timestamp.valueOf(room.getCreatedDatetime()));
        statement.setStatement("roomNumber", room.getNumber());
        statement.setStatement("roomCapacity", room.getCapacity());
        statement.setStatement("roomCostPerDay", room.getCostPerDay());
        statement.setStatement("roomRoomClassId", room.getRoomClass().getId());
        return statement;
    }

    public static NamedPreparedStatement extract(Reservation reservation, NamedPreparedStatement statement) throws SQLException {
        if (reservation.getId() != null)
            statement.setStatement("reservationId", reservation.getId());
        if (reservation.getCreatedDatetime() != null)
            statement.setStatement("reservationCreatedDatetime", Timestamp.valueOf(reservation.getCreatedDatetime()));
        statement.setStatement("reservationUserId", reservation.getUser().getId());
        if (reservation.getRoom() != null)
            statement.setStatement("reservationRoomId", reservation.getRoom().getId());
        statement.setStatement("reservationGuests", reservation.getGuests());
        statement.setStatement("reservationRequestedRoomClassId", reservation.getRequestedRoomClass().getId());
        statement.setStatement("reservationStatus", reservation.getStatus().name());
        statement.setStatement("reservationArrivalDate", Date.valueOf(reservation.getArrivalDate()));
        statement.setStatement("reservationDepartureDate", Date.valueOf(reservation.getDepartureDate()));
        return statement;
    }
}
