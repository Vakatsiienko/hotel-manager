package com.vaka.util;

import com.vaka.domain.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Created by Iaroslav on 12/5/2016.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DomainUtil {
    public static boolean hasCrucialNulls(User user) {
        return user.getRole() != null && user.getPassword() != null && user.getEmail() != null &&
                user.getName() != null && user.getPhoneNumber() != null;
    }
}
