package com.vaka.domain;

import lombok.*;

/**
 * Created by Iaroslav on 11/29/2016.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {
    private String email;
    private String password;
    private String name;
    private Role role;
    private String phoneNumber;

}
