package com.vaka.util;

import java.time.LocalDate;

/**
 * Created by Iaroslav on 12/1/2016.
 */
public class DateUtil {
    private DateUtil(){}


    /**
     * Customer can reserve if newReserveArrivalDate is >= than given reserveDepartureDate
     * or if newReserveDepartureDate <= reserveDepartureDate
     * @return true if dates are overlap
     */
    public static boolean areDatesOverlap(LocalDate reserveArrivalDate,
                                          LocalDate reserveDepartureDate,
                                          LocalDate newReserveArrivalDate,
                                          LocalDate newReserveDepartureDate) {
        return reserveArrivalDate.isAfter(newReserveDepartureDate) ||
                reserveDepartureDate.isBefore(newReserveArrivalDate) ||
                reserveArrivalDate.isEqual(newReserveDepartureDate) ||
                reserveArrivalDate.isEqual(newReserveArrivalDate);
    }
}
