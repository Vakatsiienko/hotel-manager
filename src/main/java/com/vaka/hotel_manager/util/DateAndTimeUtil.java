package com.vaka.hotel_manager.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Created by Iaroslav on 12/1/2016.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateAndTimeUtil {

    public static final DateTimeFormatter DATE_TME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");



    public static String toString(LocalDateTime dateTime) {
        return dateTime == null ? "" : dateTime.format(DATE_TME_FORMATTER);

    }

    public static String toString(LocalDate date) {
        return date == null ? "" : date.format(DATE_FORMATTER);
    }

}
