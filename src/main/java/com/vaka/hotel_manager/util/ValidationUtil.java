package com.vaka.hotel_manager.util;

import com.vaka.hotel_manager.domain.entity.Reservation;
import com.vaka.hotel_manager.domain.entity.Room;
import com.vaka.hotel_manager.domain.entity.RoomClass;
import com.vaka.hotel_manager.domain.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Util that validate object for null values(excluding nullable and generating fields)
 * and throw proper exception with proper message.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationUtil {
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


    /**
     * Checks if reservation have inappropriate fields - throwing {@link IllegalArgumentException} with problem specific message
     *
     * @param reservation checking object
     */
    public static void validate(Reservation reservation) {
        String message = null;
        if (hasNull(reservation)) {
            message = "Some reservation parameters are missing";
        } else if (reservation.getArrivalDate().compareTo(reservation.getDepartureDate()) >= 0) {
            message = "Departure date must be greater than arrival";
        } else if (reservation.getArrivalDate().isBefore(LocalDate.now())) {
            message = "You can't make reservation in past";
        } else if (reservation.getGuests() > 50 || reservation.getGuests() < 1) {
            message = "Number of guests cant be lower then 1 or greater then 50";
        }
        if (message != null)
            throw new IllegalArgumentException(message);
    }


    /**
     * Checks if user have inappropriate fields - throwing {@link IllegalArgumentException}
     *
     * @param user checking object
     */
    public static void validate(User user) {
        String message = null;
        if (hasNull(user)) {
            message = "Some user parameters are missing";
        } else if(!user.getName().matches("[\\s\\w\\.]*.*['’][\\s\\w\\.]*")){
            message = "Invalid name, only letters, spaces, dots and underscore allowed";
        } else if (getSpacesCount(user.getName()) > 3) {
            message = "Name can't have more then 3 spaces";
        } else if (user.getName().startsWith(" ")) {
            message = "Name can't starts with space";
        } else if (user.getName().length() > 30 || user.getName().length() < 5) {
            message = "User name must be greater then 5 or lower than 30 characters";
        } else if (!user.getEmail().matches(EMAIL_PATTERN)) {
            message = "Invalid email";
        } else if (user.getEmail().length() > 50) {
            message = "Email can't have more then 50 character";
        }
        if (message != null)
            throw new IllegalArgumentException(message);
    }

    public static void main(String[] args) {
        Pattern pattern = Pattern.compile("[\\s\\w\\.]*");
        Matcher matcher = pattern.matcher(" asdsa_s<adsa ");

        System.out.println(matcher.matches());
    }

    private static int getSpacesCount(String str) {
        return (int) Arrays.stream(str.split("")).filter(s -> s.equals(" ")).count();
    }

    /**
     * Checks if room have inappropriate fields - throwing {@link IllegalArgumentException}
     *
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
        if (message != null) {
            throw new IllegalArgumentException(message);
        }
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
