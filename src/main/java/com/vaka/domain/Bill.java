package com.vaka.domain;

import lombok.*;

/**
 * Created by Iaroslav on 11/23/2016.
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Bill extends BaseEntity {
    private Reservation reservation;
    private Integer totalCost;
    private boolean paid;
}
