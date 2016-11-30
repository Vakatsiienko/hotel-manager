package com.vaka.service.impl;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.vaka.domain.Manager;
import com.vaka.repository.SecurityRepository;
import com.vaka.service.SecurityService;
import com.vaka.util.ApplicationContext;
import com.vaka.util.exception.AuthenticateException;
import com.vaka.util.exception.AuthorizationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Iaroslav on 11/26/2016.
 */
public class SecurityServiceImpl implements SecurityService {
    private SecurityRepository securityRepository;
    @Override
    public Manager authenticate(HttpServletRequest req, HttpServletResponse resp) throws AuthenticateException {
        Manager user = new Manager("name", 20, "user", Base64.encode("user".getBytes()));
        user.setId(0);
        return user;
    }

    @Override
    public void authorization(HttpServletRequest req, HttpServletResponse resp) throws AuthorizationException {

    }

    @Override
    public boolean logout(HttpServletRequest req, HttpServletResponse resp, Manager user) {
        return false;
    }

    public SecurityRepository getSecurityRepository() {
        if (securityRepository == null) {
            synchronized (this) {
                if (securityRepository == null) {
                    securityRepository = ApplicationContext.getBean(SecurityRepository.class);
                }
            }
        }
        return securityRepository;
    }
}
