package com.vaka.util;

import com.vaka.domain.Reservation;
import com.vaka.domain.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Iaroslav on 12/5/2016.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DomainUtil {

    public static boolean hasNull(User user) {
        return user.getRole() == null || user.getPassword() == null || user.getEmail() == null ||
                user.getName() == null || user.getPhoneNumber() == null;
    }

    public static boolean hasNull(Reservation reservation) {
        return reservation.getStatus() == null || reservation.getArrivalDate() == null ||
                reservation.getDepartureDate() == null || reservation.getGuests() == null ||
                reservation.getRequestedRoomClass() == null;
    }
}
