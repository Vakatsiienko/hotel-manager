package com.vaka.hotel_manager.service;

import com.vaka.hotel_manager.domain.Role;
import com.vaka.hotel_manager.domain.User;
import com.vaka.hotel_manager.util.exception.AuthenticationException;
import com.vaka.hotel_manager.util.exception.AuthorizationException;

import javax.servlet.http.HttpSession;
import java.util.Set;

/**
 * Created by Iaroslav on 11/26/2016.
 */
public interface SecurityService {

    /**
     * @param session to which add loggedUser
     * @param email email of user which sign in
     * @param password non encrypted password
     * @throws AuthenticationException if credentials do not match
     */
    void signIn(HttpSession session, String email, String password) throws AuthenticationException;

    /**
     * @param session where loggedUser stored
     * @return stored loggedUser or anonymous user if no user exist in given session
     */
    User authenticate(HttpSession session);

    /**
     * Invalidate session
     * @param session session which will be invalidated
     */
    void logout(HttpSession session);

    /**
     * Check user role by expectedRoles, if user role do not contains in expectedRoles method threw AuthorizationException
     * @param loggedUser user which will be checked
     * @param expectedRoles roles which have permission to proceed
     * @throws AuthorizationException
     */
    void authorize(User loggedUser, Set<Role> expectedRoles) throws AuthorizationException;

}
