package com.vaka.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;

/**
 * Created by Iaroslav on 11/27/2016.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Reservation extends BaseEntity {

    private User user;

    private Room room;

    private Integer guests;

    private RoomClass requestedRoomClass;

    private ReservationStatus status;

    private LocalDate arrivalDate;

    private LocalDate departureDate;

}
