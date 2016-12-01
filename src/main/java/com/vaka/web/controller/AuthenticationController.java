package com.vaka.web.controller;

import com.vaka.domain.User;
import com.vaka.service.SecurityService;
import com.vaka.util.ApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Iaroslav on 12/1/2016.
 */
public class AuthenticationController {

    private SecurityService securityService;

    public void createToken(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        if (email != null && password != null) {
            getSecurityService().createToken(req, resp, email, password);
            resp.sendRedirect("/");
        }
        else resp.setStatus(400);
    }

    public void signinPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("loggedUser", getSecurityService().authenticate(req, resp));
        req.getRequestDispatcher("/signin.jsp").forward(req, resp);
    }




    private SecurityService getSecurityService() {
        if (securityService == null) {
            synchronized (this) {
                if (securityService == null) {
                    securityService = ApplicationContext.getBean(SecurityService.class);
                }
            }
        }
        return securityService;
    }
}
