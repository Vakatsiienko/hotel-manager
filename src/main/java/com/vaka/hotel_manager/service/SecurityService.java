package com.vaka.hotel_manager.service;

import com.vaka.hotel_manager.domain.User;
import com.vaka.hotel_manager.util.exception.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Iaroslav on 11/26/2016.
 */
public interface SecurityService {


    void signIn(HttpServletRequest req, HttpServletResponse resp, String email, String password) throws AuthenticationException;

    User authenticate(HttpServletRequest req, HttpServletResponse resp);

    void logout(HttpServletRequest req, HttpServletResponse resp, User loggedUser);

}
