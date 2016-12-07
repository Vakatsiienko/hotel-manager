package com.vaka.service;

import com.vaka.domain.Role;
import com.vaka.domain.User;
import com.vaka.util.exception.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Iaroslav on 11/26/2016.
 */
public interface SecurityService {



    void signIn(HttpServletRequest req, HttpServletResponse resp, String email, String password) throws AuthenticationException;

    User authenticate(HttpServletRequest req, HttpServletResponse resp);

    void logout(HttpServletRequest req, HttpServletResponse resp, User loggedUser);

    void authorize(User loggedUser, Role expected);

}
