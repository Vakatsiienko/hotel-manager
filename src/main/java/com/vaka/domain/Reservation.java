package com.vaka.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Period;

/**
 * Created by Iaroslav on 11/27/2016.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation extends BaseEntity {

    private Manager user;

    private Room room;

    private Period period;

    private ReservationRequest request;

}
