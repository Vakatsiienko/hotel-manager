package com.vaka.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Created by Iaroslav on 12/1/2016.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtil {

    /**
     * Customer can reserve if newReserveArrivalDate is >= than given reserveDepartureDate
     * or if newReserveDepartureDate <= reserveDepartureDate
     *
     * @return true if dates are overlap
     */
    public static boolean areDatesOverlap(LocalDate reserveArrivalDate,
                                          LocalDate reserveDepartureDate,
                                          LocalDate newReserveArrivalDate,
                                          LocalDate newReserveDepartureDate) {
        return reserveArrivalDate.isAfter(newReserveDepartureDate) ||
                reserveDepartureDate.isBefore(newReserveArrivalDate) ||
                reserveArrivalDate.isEqual(newReserveDepartureDate) ||
                reserveDepartureDate.isEqual(newReserveArrivalDate);
    }
}
