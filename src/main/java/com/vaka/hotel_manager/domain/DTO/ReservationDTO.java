package com.vaka.hotel_manager.domain.dto;

import com.vaka.hotel_manager.domain.ReservationStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by Iaroslav on 12/10/2016.
 */
@Getter
@ToString
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class ReservationDTO {
    private final Integer id;

    private final LocalDateTime createdDatetime;

    private final Integer userId;

    private final Integer roomId; //TODO make Optional roomId

    private final Integer guests;

    private final ReservationStatus status;

    private final RoomClassDTO requestedRoomClass;

    private final LocalDate arrivalDate;

    private final LocalDate departureDate;
}
