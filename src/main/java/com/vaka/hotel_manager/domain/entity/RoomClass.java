package com.vaka.hotel_manager.domain.entity;

import lombok.*;

/**
 * Created by Iaroslav on 11/26/2016.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class RoomClass extends BaseEntity {
    private String name;
}
