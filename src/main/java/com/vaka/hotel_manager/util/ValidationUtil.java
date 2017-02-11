package com.vaka.hotel_manager.util;

import com.vaka.hotel_manager.domain.entity.Reservation;
import com.vaka.hotel_manager.domain.entity.Room;
import com.vaka.hotel_manager.domain.entity.RoomClass;
import com.vaka.hotel_manager.domain.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.StringJoiner;
import java.util.regex.Pattern;

/**
 * Util that validate object for null values(excluding nullable and generating fields)
 * and throw proper exception with proper message.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationUtil {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[_A-Za-z0-9-\\+\\p{L}]+(\\.[_A-Za-z0-9-\\p{L}]+)*@"
            + "[A-Za-z0-9-\\p{L}]+(\\.[A-Za-z0-9\\p{L}]+)*(\\.[A-Za-z\\p{L}]{2,})$");

    /**
     * Checks if reservation have inappropriate fields - throwing {@link IllegalArgumentException} with problem specific message
     *
     * @param reservation checking object
     */
    public static void validate(Reservation reservation) {
        StringJoiner messageJoiner = new StringJoiner("\n");
        try {
            if (hasNull(reservation)) {
                messageJoiner.add("Some reservation parameters are missing");
                return;
            }
            if (reservation.getArrivalDate().compareTo(reservation.getDepartureDate()) >= 0) {
                messageJoiner.add("Departure date must be greater than arrival");
            }
            if (reservation.getArrivalDate().isBefore(LocalDate.now())) {
                messageJoiner.add("You can't make reservation in past");
            }
            if (reservation.getGuests() > 50 || reservation.getGuests() < 1) {
                messageJoiner.add("Number of guests cant be lower then 1 or greater then 50");
            }
        } finally {
            if (messageJoiner.length() != 0) {
                throw new IllegalArgumentException(messageJoiner.toString());
            }
        }
    }


    private static boolean hasNull(Reservation reservation) {
        return reservation.getArrivalDate() == null ||
                reservation.getDepartureDate() == null || reservation.getGuests() == null ||
                reservation.getRequestedRoomClass() == null ||
                reservation.getRequestedRoomClass().getId() == null;
    }

    /**
     * Checks if user have inappropriate fields - throwing {@link IllegalArgumentException}
     *
     * @param user checking object
     */
    public static void validate(User user) {
        StringJoiner messageJoiner = new StringJoiner("\n");
        try {
            if (hasNull(user)) {
                messageJoiner.add("Some user parameters are missing");
                return;
            }
//            if (!user.getName().matches("[\\s\\w\\.'’`\\p{L}\\-]*?")) {//TODO check html pattern regexp
//                message = "Invalid name, only letters, spaces, and .'’`- allowed";
            if (user.getName().length() >= 40 || user.getName().length() < 5) {
                messageJoiner.add("User name must be greater then 5 or lower than 40 characters");
            }
            if (!EMAIL_PATTERN.matcher(user.getEmail()).matches()) {
                messageJoiner.add("Invalid email");
            }
            if (user.getEmail().length() > 50) {
                messageJoiner.add("Email can't have more then 50 character");
            }
        } finally {
            if (messageJoiner.length() != 0) {
                throw new IllegalArgumentException(messageJoiner.toString());
            }
        }


    }

    private static boolean hasNull(User user) {
        return user.getRole() == null || user.getPassword() == null || user.getEmail() == null ||
                user.getName() == null || user.getPhoneNumber() == null;
    }

    /**
     * Checks if room have inappropriate fields - throwing {@link IllegalArgumentException}
     *
     * @param room checking object
     */
    public static void validate(Room room) {
        StringJoiner messageJoiner = new StringJoiner("\n");
        try {
            if (hasNull(room)) {
                messageJoiner.add("Some room parameters are missing");
                return;
            }
            if (room.getCapacity() < 1) {
                messageJoiner.add("Capacity cant be lower than 1");
            }
            if (room.getCostPerDay() < 100) {
                messageJoiner.add("Cost per day cant be lower than 1");
            }
        } finally {
            if (messageJoiner.length() != 0) {
                throw new IllegalArgumentException(messageJoiner.toString());
            }
        }
    }

    private static boolean hasNull(RoomClass roomClass) {
        return roomClass.getName() == null;
    }

    private static boolean hasNull(Room room) {
        return room.getCapacity() == null || room.getCostPerDay() == null ||
                room.getNumber() == null || room.getRoomClass() == null || room.getRoomClass().getId() == null;
    }
}
