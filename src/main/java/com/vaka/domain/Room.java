package com.vaka.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Iaroslav on 11/23/2016.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Room extends BaseEntity {
    private Integer number;
    private Integer capacity;
    private Integer costPerDay;
    private RoomClass roomClazz;
    private String description;
}
