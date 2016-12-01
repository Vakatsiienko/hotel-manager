package com.vaka.web.controller;

import com.vaka.domain.RoomClass;
import com.vaka.service.SecurityService;
import com.vaka.util.ApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Iaroslav on 12/1/2016.
 */
public class MainController {
    private SecurityService securityService;

    public void toRoot(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("loggedUser", getSecurityService().authenticate(req, resp));
        req.setAttribute("roomClazzez", RoomClass.values());
        req.getRequestDispatcher("/home.jsp").forward(req, resp);
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
