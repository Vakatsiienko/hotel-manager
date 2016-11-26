package com.vaka.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

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
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer totalCost;
    private String commentary;
    private ReservationRequestStatus status;


}
