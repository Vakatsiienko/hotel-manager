package com.vaka.hotel_manager.domain.DTO;

import com.vaka.hotel_manager.domain.ReservationStatus;
import com.vaka.hotel_manager.domain.RoomClass;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by Iaroslav on 12/10/2016.
 */
@Getter
@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class ReservationDTO {
    private final Integer id;

    private final LocalDateTime createdDatetime;

    private final Integer userId;

    private final Integer roomId;

    private final Integer guests;

    private final ReservationStatus status;

    private final RoomClass requestedRoomClass;

    private final LocalDate arrivalDate;

    private final LocalDate departureDate;
}
