package com.vaka.hotel_manager.web.controller;

import com.vaka.hotel_manager.core.context.ApplicationContext;
import com.vaka.hotel_manager.domain.RoomClass;
import com.vaka.hotel_manager.domain.User;
import com.vaka.hotel_manager.service.RoomClassService;
import com.vaka.hotel_manager.core.security.SecurityService;
import com.vaka.hotel_manager.util.ServletExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Iaroslav on 1/26/2017.
 */
public class RoomClassController {
    private static final Logger LOG = LoggerFactory.getLogger(RoomClassController.class);
    private RoomClassService roomClassService;

    private SecurityService securityService;

    public void create(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User loggedUser = getSecurityService().authenticate(req.getSession());
        LOG.debug("Creating RoomClass");
        RoomClass roomClass = ServletExtractor.extractRoomClass(req);
        getRoomClassService().create(loggedUser, roomClass);
        resp.sendRedirect("/room-classes");
    }

    public void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User loggedUser = getSecurityService().authenticate(req.getSession());
        LOG.debug("Deleting RoomClass");
        Integer id = Integer.valueOf(req.getRequestURI().split("/room-classes/")[1]);
        boolean deleted = getRoomClassService().delete(loggedUser, id);
        if (deleted)
            resp.sendRedirect("/room-classes");
        else {
            req.setAttribute("message", "roomClass.falseDelete");
            req.setAttribute("roomClassList", getRoomClassService().findAll(loggedUser));
            req.getRequestDispatcher("/jsp/roomClassesPage.jsp").forward(req, resp);
        }
    }

    public void update(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User loggedUser = getSecurityService().authenticate(req.getSession());
        LOG.debug("Updating RoomClass");
        RoomClass roomClass = ServletExtractor.extractRoomClass(req);
        Integer id = Integer.valueOf(req.getRequestURI().split("/room-classes/")[1]);
        getRoomClassService().update(loggedUser, id, roomClass);
        resp.sendRedirect("/room-classes");
    }

    public void page(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User loggedUser = getSecurityService().authenticate(req.getSession());
        LOG.debug("To RoomClass page");
        req.setAttribute("roomClassList", getRoomClassService().findAll(loggedUser));
        req.getRequestDispatcher("/jsp/roomClassesPage.jsp").forward(req, resp);
    }

    public SecurityService getSecurityService() {
        if (securityService == null) {
            synchronized (this) {
                if (securityService == null) {
                    securityService = ApplicationContext.getInstance().getBean(SecurityService.class);
                }
            }
        }
        return securityService;
    }

    public RoomClassService getRoomClassService() {
        if (roomClassService == null) {
            synchronized (this) {
                if (roomClassService == null) {
                    roomClassService = ApplicationContext.getInstance().getBean(RoomClassService.class);
                }
            }
        }
        return roomClassService;
    }
}
