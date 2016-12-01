package com.vaka.web.servlet;

import com.vaka.util.ApplicationContext;
import com.vaka.web.controller.AuthenticationController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Iaroslav on 12/1/2016.
 */
public class AuthenticationServlet extends HttpServlet {
    private AuthenticationController authenticationController;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getRequestURI().equals("/signin")) {
            getAuthenticationController().signinPage(req, resp);
        } else resp.setStatus(400);
        if (resp.getStatus() == 400)
            super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getRequestURI().equals("/signin")) {
            getAuthenticationController().createToken(req, resp);
        } else resp.setStatus(400);
        if (resp.getStatus() == 400)
            super.doPost(req, resp);
    }

    public AuthenticationController getAuthenticationController() {
        if (authenticationController == null) {
            synchronized (this) {
                if (authenticationController == null) {
                    authenticationController = ApplicationContext.getBean(AuthenticationController.class);
                }
            }
        }
        return authenticationController;
    }
}
