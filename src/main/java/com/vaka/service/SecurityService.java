package com.vaka.service;

import com.vaka.domain.Manager;
import com.vaka.util.exception.AuthenticateException;
import com.vaka.util.exception.AuthorizationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Iaroslav on 11/26/2016.
 */
public interface SecurityService {
    Manager authenticate(HttpServletRequest req, HttpServletResponse resp) throws AuthenticateException;

    void authorization(HttpServletRequest req, HttpServletResponse resp) throws AuthorizationException;

    boolean logout(HttpServletRequest req, HttpServletResponse resp, Manager user);

}
