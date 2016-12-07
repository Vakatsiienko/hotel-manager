package com.vaka.hotel_manager.domain;

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
public class Room extends BaseEntity {
    private Integer number;
    private Integer capacity;
    private Integer costPerDay;
    private RoomClass roomClazz;
    private String description;
}
