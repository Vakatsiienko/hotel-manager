package com.vaka.util;

import com.vaka.domain.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * Created by Iaroslav on 11/26/2016.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DomainExtractor {

    public static Reservation extractReservation(ResultSet resultSet) throws SQLException {
        Reservation reservation = new Reservation();
        if (resultSet.getString("room_class") != null) {
            reservation.setRoom(extractRoom(resultSet)); //TODO add prefix to extraction methods for inner entities
            reservation.getRoom().setId(resultSet.getInt("room_id"));
        }
        reservation.setUser(extractUser(resultSet));
        reservation.getUser().setId(resultSet.getInt("user_id"));
        reservation.setId(resultSet.getInt("id"));
        reservation.setCreatedDatetime(resultSet.getTimestamp("created_datetime").toInstant().atZone(ZoneId.of("UTC")).toLocalDateTime());
        reservation.setGuests(resultSet.getInt("guests"));
        reservation.setRequestedRoomClass(RoomClass.valueOf(resultSet.getString("requested_room_class")));
        reservation.setStatus(ReservationStatus.valueOf(resultSet.getString("status")));
        reservation.setArrivalDate(resultSet.getDate("arrival_date").toLocalDate());
        reservation.setDepartureDate(resultSet.getDate("departure_date").toLocalDate());
        return reservation;
    }

    public static Room extractRoom(ResultSet resultSet) throws SQLException {
        Room room = new Room();
        room.setId(resultSet.getInt("id"));
        room.setCreatedDatetime(resultSet.getTimestamp("created_datetime").toInstant().atZone(ZoneId.of("UTC")).toLocalDateTime());
        room.setNumber(resultSet.getInt("number"));
        room.setCapacity(resultSet.getInt("capacity"));
        room.setCostPerDay(resultSet.getInt("cost_per_day"));
        room.setRoomClazz(RoomClass.valueOf(resultSet.getString("room_class")));
        room.setDescription(resultSet.getString("description"));
        return room;
    }

    public static Bill extractBill(ResultSet resultSet) throws SQLException {
        Bill bill = new Bill();
        bill.setId(resultSet.getInt("id"));
        bill.setCreatedDatetime(resultSet.getTimestamp("created_datetime").toInstant().atZone(ZoneId.of("UTC")).toLocalDateTime());
        bill.setReservation(extractReservation(resultSet));
        bill.getReservation().setId(resultSet.getInt("reservation_id"));
        bill.setTotalCost(resultSet.getInt("total_cost"));
        bill.setPaid(resultSet.getBoolean("paid"));
        return bill;
    }

    public static User extractUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setCreatedDatetime(resultSet.getTimestamp("created_datetime").toInstant().atZone(ZoneId.of("UTC")).toLocalDateTime());
        user.setRole(Role.valueOf(resultSet.getString("role")));
        user.setPassword(resultSet.getString("password"));
        user.setPhoneNumber(resultSet.getString("phone_number"));
        user.setName(resultSet.getString("name"));
        user.setEmail(resultSet.getString("email"));
        return user;
    }

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
