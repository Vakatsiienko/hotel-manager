package com.vaka.hotel_manager.util;

import com.vaka.hotel_manager.domain.Reservation;
import com.vaka.hotel_manager.domain.Room;
import com.vaka.hotel_manager.domain.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Created by Iaroslav on 12/5/2016.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IntegrityUtil {

    /**
     * Checks if reservation have inappropriate fields - throwing {@link IllegalArgumentException} with problem specific message
     * @param reservation checking object
     */
    public static void check(Reservation reservation) throws IllegalArgumentException {
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
    public static void check(User user) throws IllegalArgumentException {
        String message = null;
        if (hasNull(user))
            message = "Some user parameters are missing";
        if (message != null)
            throw new IllegalArgumentException(message);
    }
    /**
     * Checks if room have inappropriate fields - throwing {@link IllegalArgumentException}
     * @param room checking object
     */
    public static void check(Room room) throws IllegalArgumentException {
        String message = null;
        if (hasNull(room))
            message = "Some user parameters are missing";
        if (message != null)
            throw new IllegalArgumentException(message);
    }

    private static boolean hasNull(Room room) {
        return room.getCapacity() == null || room.getCostPerDay() == null ||
                room.getNumber() == null || room.getRoomClazz() == null;
    }

    private static boolean hasNull(User user) {
        return user.getRole() == null || user.getPassword() == null || user.getEmail() == null ||
                user.getName() == null || user.getPhoneNumber() == null;
    }

    private static boolean hasNull(Reservation reservation) {
        return reservation.getStatus() == null || reservation.getArrivalDate() == null ||
                reservation.getDepartureDate() == null || reservation.getGuests() == null ||
                reservation.getRequestedRoomClass() == null;
    }
}
