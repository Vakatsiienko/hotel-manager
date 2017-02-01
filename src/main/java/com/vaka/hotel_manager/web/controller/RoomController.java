package com.vaka.hotel_manager.web.controller;

import com.vaka.hotel_manager.core.context.ApplicationContext;
import com.vaka.hotel_manager.domain.Room;
import com.vaka.hotel_manager.domain.User;
import com.vaka.hotel_manager.service.RoomClassService;
import com.vaka.hotel_manager.service.RoomService;
import com.vaka.hotel_manager.core.security.SecurityService;
import com.vaka.hotel_manager.util.ValidationUtil;
import com.vaka.hotel_manager.util.ServletExtractor;
import com.vaka.hotel_manager.util.exception.CreatingException;
import com.vaka.hotel_manager.util.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * Created by Iaroslav on 11/25/2016.
 */
public class RoomController {
    private static final Logger LOG = LoggerFactory.getLogger(RoomController.class);

    private RoomClassService roomClassService;
    private RoomService roomService;
    private SecurityService securityService;

    public void roomsPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User loggedUser = getSecurityService().authenticate(req.getSession());
        LOG.debug("To rooms page");
        Integer page = ServletExtractor.extractOrDefault(req.getParameter("page"), 1, Integer::parseInt);
        Integer rows = ServletExtractor.extractOrDefault(req.getParameter("size"), 10, Integer::parseInt);

        req.setAttribute("roomClasses", getRoomClassService().findAll(loggedUser));
        req.setAttribute("roomPage", getRoomService().findPage(loggedUser, page, rows));
        req.getRequestDispatcher("/jsp/roomsPage.jsp").forward(req, resp);
    }

    public void create(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User loggedUser = getSecurityService().authenticate(req.getSession());
        LOG.debug("Creating room request");
        Room room = ServletExtractor.extractRoom(req);
        ValidationUtil.validate(room);
        try {
            room = getRoomService().create(loggedUser, room);
            req.getSession().removeAttribute("sessionRoom");
        } catch (CreatingException e){
            LOG.debug(e.getMessage(), e);
            req.getSession().setAttribute("sessionRoom", room);
            req.setAttribute("message", "room.creatingNumberExist");
            req.setAttribute("roomPage", getRoomClassService().findAll(loggedUser));
            req.setAttribute("roomPage", getRoomService().findPage(loggedUser, 1, 10));
            req.getRequestDispatcher("/roomsPage.jsp").forward(req, resp);
            return;
        }
        req.setAttribute("room", room);
        LOG.debug("To room page, roomId: {}", room.getId());
        resp.sendRedirect("/rooms/" + room.getId());
    }

    public void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User loggedUser = getSecurityService().authenticate(req.getSession());
        LOG.debug("Delete room request");
        Integer roomId = Integer.valueOf(req.getRequestURI().split("/rooms/")[1]);
        getRoomService().delete(loggedUser, roomId);
        LOG.debug("To rooms page");
        resp.sendRedirect("/rooms");
    }

    public void roomPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User loggedUser = getSecurityService().authenticate(req.getSession());
        LOG.debug("To room page");
        Integer roomId = Integer.valueOf(req.getRequestURI().split("/rooms/")[1]);
        Optional<Room> room = getRoomService().getById(loggedUser, roomId);
        if (!room.isPresent())
            throw new NotFoundException("Room not found");
        req.setAttribute("room", room.get());
        req.setAttribute("roomClasses", getRoomClassService().findAll(loggedUser));
        req.getRequestDispatcher("/jsp/roomInfo.jsp").forward(req, resp);

    }

    public void update(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User loggedUser = getSecurityService().authenticate(req.getSession());
        LOG.debug("Updating room");
        Room room = ServletExtractor.extractRoom(req);
        ValidationUtil.validate(room);
        Integer id = Integer.valueOf(req.getRequestURI().split("/rooms/")[1]);
        getRoomService().update(loggedUser, id, room);
        LOG.debug("To room page, roomId: {}", id);
        resp.sendRedirect("/rooms/" + id);
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
    public RoomService getRoomService() {
        if (roomService == null)
            synchronized (this) {
                if (roomService == null) {
                    roomService = ApplicationContext.getInstance().getBean(RoomService.class);
                }
            }
        return roomService;
    }
}
