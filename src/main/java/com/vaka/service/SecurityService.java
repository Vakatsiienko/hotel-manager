package com.vaka.service;

import com.vaka.domain.Role;
import com.vaka.domain.User;
import com.vaka.domain.Manager;
import com.vaka.domain.User;
import com.vaka.util.exception.AuthenticateException;
import com.vaka.util.exception.AuthorizationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Iaroslav on 11/26/2016.
 */
public interface SecurityService {

    void createToken(HttpServletRequest req, HttpServletResponse resp, User loggedUser);

    User checkCredentials(HttpServletRequest req, HttpServletResponse resp, String email, String password) throws AuthenticateException;

    User authenticate(HttpServletRequest req, HttpServletResponse resp);

    void logout(HttpServletRequest req, HttpServletResponse resp, String token);

    void authorize(User loggedUser, Role expected);

}
