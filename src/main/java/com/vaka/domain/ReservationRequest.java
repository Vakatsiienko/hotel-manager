package com.vaka.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;

/**
 * Created by Iaroslav on 11/23/2016.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequest extends BaseEntity {
    private User customer;
    private Integer numOfBeds;
    private RoomClass roomClass;
    private BathroomType bathroomType;
    private Period period;
    private Integer totalCost;
    private String commentary;
    private ReservationRequestStatus status;


}
