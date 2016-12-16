package com.vaka.hotel_manager.util;

import com.vaka.hotel_manager.domain.Role;
import com.vaka.hotel_manager.domain.User;
import com.vaka.hotel_manager.util.exception.AuthorizationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * Created by Iaroslav on 12/6/2016.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUtil {
    public static final Set<Role> MANAGER_ACCESS_ROLES;
    public static final Set<Role> CUSTOMER_ACCESS_ROLES;
    public static final Set<Role> ANONYMOUS_ACCESS_ROLES;
    static{
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

    public static User eraseSensitivityCredentials(User user) {
        user.setPassword("");
        return user;
    }
    public static String generatePassword(User user) {
        return BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
    }
}
