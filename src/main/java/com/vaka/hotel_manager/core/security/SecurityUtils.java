package com.vaka.hotel_manager.core.security;

import com.vaka.hotel_manager.domain.Role;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * Created by Iaroslav on 12/6/2016.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUtils {

    public static final Set<Role> MANAGER_ACCESS_ROLES;
    public static final Set<Role> CUSTOMER_ACCESS_ROLES;
    public static final Set<Role> ANONYMOUS_ACCESS_ROLES;

    static {
        Set<Role> managerRoleSet = new HashSet<>();
        managerRoleSet.add(Role.MANAGER);
        MANAGER_ACCESS_ROLES = Collections.unmodifiableSet(managerRoleSet);


        Set<Role> customerRoleSet = new HashSet<>();
        customerRoleSet.add(Role.MANAGER);
        customerRoleSet.add(Role.CUSTOMER);
        CUSTOMER_ACCESS_ROLES = Collections.unmodifiableSet(customerRoleSet);

        Set<Role> anonymousRoleSet = new HashSet<>();
        anonymousRoleSet.add(Role.ANONYMOUS);
        anonymousRoleSet.add(Role.CUSTOMER);
        anonymousRoleSet.add(Role.MANAGER);
        ANONYMOUS_ACCESS_ROLES = Collections.unmodifiableSet(anonymousRoleSet);
    }
}
