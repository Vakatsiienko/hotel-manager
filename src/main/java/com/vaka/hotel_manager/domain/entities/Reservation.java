package com.vaka.hotel_manager.domain.entities;

import com.vaka.hotel_manager.domain.ReservationStatus;
import lombok.*;

import java.time.LocalDate;

/**
 * Created by Iaroslav on 11/27/2016.
 */
@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Reservation extends BaseEntity {

    private User user;

    private Room room;

    private Integer guests;

    private RoomClass requestedRoomClass;

    private ReservationStatus status;

    private LocalDate arrivalDate;

    private LocalDate departureDate;

}
