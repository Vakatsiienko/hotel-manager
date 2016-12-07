package com.vaka.hotel_manager.web.controller;

import com.vaka.hotel_manager.domain.RoomClass;
import com.vaka.hotel_manager.domain.User;
import com.vaka.hotel_manager.service.SecurityService;
import com.vaka.hotel_manager.context.ApplicationContext;

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
        User user = getSecurityService().authenticate(req, resp);
        req.setAttribute("loggedUser", user);
        req.setAttribute("roomClasses", RoomClass.values());
        req.getRequestDispatcher("/home.jsp").forward(req, resp);
    }

    private SecurityService getSecurityService() {
        if (securityService == null) {
            synchronized (this) {
                if (securityService == null) {
                    securityService = ApplicationContext.getInstance().getBean(SecurityService.class);
                }
            }
        }
        return securityService;
    }
}
