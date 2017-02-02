package com.vaka.hotel_manager.util;

import com.vaka.hotel_manager.domain.entities.Reservation;
import com.vaka.hotel_manager.domain.entities.Room;
import com.vaka.hotel_manager.domain.entities.RoomClass;
import com.vaka.hotel_manager.domain.entities.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Util that validate object for null values(excluding nullable and generating fields)
 * and throw proper exception with proper message.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationUtil {

    /**
     * Checks if reservation have inappropriate fields - throwing {@link IllegalArgumentException} with problem specific message
     * @param reservation checking object
     */
    public static void validate(Reservation reservation) {
        String message = null;
        if (hasNull(reservation))
            message = "Some reservation parameters are missing";
        else if (reservation.getArrivalDate().compareTo(reservation.getDepartureDate()) >= 0)
            message = "Departure date must be greater than arrival";
        else if (reservation.getArrivalDate().isBefore(LocalDate.now()))
            message = "You can't make reservation in past";
        if (message != null)
            throw new IllegalArgumentException(message);
    }
    /**
     * Checks if user have inappropriate fields - throwing {@link IllegalArgumentException}
     * @param user checking object
     */
    public static void validate(User user) {
        String message = null;
        if (hasNull(user)) {
            message = "Some user parameters are missing";
        } else if (user.getName().length() > 30 || user.getName().length() < 5) {
            message = "User name must be greater then 5 or lower than 30 characters";
        }
        if (message != null)
            throw new IllegalArgumentException(message);
    }
    /**
     * Checks if room have inappropriate fields - throwing {@link IllegalArgumentException}
     * @param room checking object
     */
    public static void validate(Room room) {
        String message = null;
        if (hasNull(room)) {
            message = "Some room parameters are missing";
        } else if (room.getCapacity() < 1) {
            message = "Capacity cant be lower than 1";
        } else if (room.getCostPerDay() < 100) {
            message = "Cost per day cant be lower than 1 dollar";
        }
        if (message != null)
            throw new IllegalArgumentException(message);

    }

    private static boolean hasNull(RoomClass roomClass) {
        return roomClass.getName() == null;
    }

    private static boolean hasNull(Room room) {
        return room.getCapacity() == null || room.getCostPerDay() == null ||
                room.getNumber() == null || room.getRoomClass() == null;
    }

    private static boolean hasNull(User user) {
        return user.getRole() == null || user.getPassword() == null || user.getEmail() == null ||
                user.getName() == null || user.getPhoneNumber() == null;
    }

    private static boolean hasNull(Reservation reservation) {
        return reservation.getArrivalDate() == null ||
                reservation.getDepartureDate() == null || reservation.getGuests() == null ||
                reservation.getRequestedRoomClass().getName() == null;
    }
}
