package com.vaka.hotel_manager.util.repository;

import com.vaka.hotel_manager.domain.*;
import com.vaka.hotel_manager.domain.DTO.ReservationDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

/**
 * Created by Iaroslav on 11/26/2016.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StatementToDomainExtractor {

    public static ReservationDTO extractReservationDTO(ResultSet resultSet) throws SQLException {
        Integer id = resultSet.getInt("reservation_id");
        LocalDateTime createdDateTime = resultSet.getTimestamp("reservation_created_datetime").toLocalDateTime();
        Integer userId = resultSet.getInt("user_id");
        Integer roomId = resultSet.getInt("room_id");
        if (roomId == 0)
            roomId = null;
        Integer guests = resultSet.getInt("reservation_guests");
        ReservationStatus status = ReservationStatus.valueOf(resultSet.getString("reservation_status"));
        RoomClass requestedRoomClass = RoomClass.valueOf(resultSet.getString("reservation_requested_room_class"));
        LocalDate arrivalDate = resultSet.getDate("reservation_arrival_date").toLocalDate();
        LocalDate departureDate = resultSet.getDate("reservation_departure_date").toLocalDate();
        return new ReservationDTO(id, createdDateTime, userId, roomId, guests, status, requestedRoomClass, arrivalDate, departureDate);
    }

    public static Reservation extractReservation(ResultSet resultSet) throws SQLException {
        Reservation reservation = new Reservation();
        if (resultSet.getString("room_room_class") != null) {
            reservation.setRoom(extractRoom(resultSet));
        }
        reservation.setUser(extractUser(resultSet));
        reservation.setId(resultSet.getInt("reservation_id"));
        reservation.setCreatedDatetime(resultSet.getTimestamp("reservation_created_datetime").toLocalDateTime());
        reservation.setGuests(resultSet.getInt("reservation_guests"));
        reservation.setRequestedRoomClass(RoomClass.valueOf(resultSet.getString("reservation_requested_room_class")));
        reservation.setStatus(ReservationStatus.valueOf(resultSet.getString("reservation_status")));
        reservation.setArrivalDate(resultSet.getDate("reservation_arrival_date").toLocalDate());
        reservation.setDepartureDate(resultSet.getDate("reservation_departure_date").toLocalDate());
        return reservation;
    }

    public static Room extractRoom(ResultSet resultSet) throws SQLException {
        Room room = new Room();
        room.setId(resultSet.getInt("room_id"));
        room.setCreatedDatetime(resultSet.getTimestamp("room_created_datetime").toLocalDateTime());
        room.setNumber(resultSet.getInt("room_number"));
        room.setCapacity(resultSet.getInt("room_capacity"));
        room.setCostPerDay(resultSet.getInt("room_cost_per_day"));
        room.setRoomClazz(RoomClass.valueOf(resultSet.getString("room_room_class")));
        return room;
    }

    public static Bill extractBill(ResultSet resultSet) throws SQLException {
        Bill bill = new Bill();
        bill.setId(resultSet.getInt("bill_id"));
        bill.setCreatedDatetime(resultSet.getTimestamp("bill_created_datetime").toLocalDateTime());
        bill.setReservation(extractReservation(resultSet));
        bill.setTotalCost(resultSet.getInt("bill_total_cost"));
        bill.setPaid(resultSet.getBoolean("bill_paid"));
        return bill;
    }

    public static User extractUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("user_id"));
        user.setCreatedDatetime(resultSet.getTimestamp("user_created_datetime").toLocalDateTime());
        user.setRole(Role.valueOf(resultSet.getString("user_role")));
        user.setPassword(resultSet.getString("user_password"));
        user.setPhoneNumber(resultSet.getString("user_phone_number"));
        user.setName(resultSet.getString("user_name"));
        user.setEmail(resultSet.getString("user_email"));
        return user;
    }

}
