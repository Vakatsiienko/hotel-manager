package com.vaka.hotel_manager.domain;

import lombok.*;

import java.io.Serializable;


/**
 * Created by Iaroslav on 11/29/2016.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"password"})
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity implements Serializable {
    private String email;
    private String password;
    private String name;
    private String vkId;
    private Role role;
    private String phoneNumber;
}
