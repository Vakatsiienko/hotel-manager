package com.vaka.service.impl;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.vaka.domain.User;
import com.vaka.service.SecurityService;
import com.vaka.util.exception.AuthenticateException;
import com.vaka.util.exception.AuthorizationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Iaroslav on 11/26/2016.
 */
public class SecurityServiceImpl implements SecurityService {
    @Override
    public User authenticate(HttpServletRequest req, HttpServletResponse resp) throws AuthenticateException {
        User user = new User("name", 20, true, "user", Base64.encode("user".getBytes()));
        user.setId(0);
        return user;
    }

    @Override
    public void authorization(HttpServletRequest req, HttpServletResponse resp) throws AuthorizationException {

    }

    @Override
    public boolean logout(HttpServletRequest req, HttpServletResponse resp, User user) {
        return false;
    }
}
