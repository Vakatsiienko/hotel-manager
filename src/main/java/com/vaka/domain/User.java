package com.vaka.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Iaroslav on 11/29/2016.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {
    private String email;
    private String password;
    private String name;
    private Role role;
    private String phoneNumber;
}
