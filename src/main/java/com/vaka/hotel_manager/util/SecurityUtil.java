package com.vaka.hotel_manager.util;

import com.vaka.hotel_manager.domain.Role;
import com.vaka.hotel_manager.domain.User;
import com.vaka.hotel_manager.util.exception.AuthorizationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Base64;

/**
 * Created by Iaroslav on 12/6/2016.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUtil {
    public static void authorize(User loggedUser, Role expected) {
        if (expected != loggedUser.getRole() && loggedUser.getRole() != Role.MANAGER) {
            throw new AuthorizationException(String.format("Expected role: %s, actual: %s", expected, loggedUser.getRole()));
        }
    }

    public static String generatePassword(User user) {
        return Base64.getEncoder().encodeToString(
                String.join(":", user.getEmail(), user.getPassword())
                        .getBytes());

    }
}
