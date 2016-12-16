package com.vaka.hotel_manager.web.controller;

import com.vaka.hotel_manager.context.ApplicationContext;
import com.vaka.hotel_manager.domain.Room;
import com.vaka.hotel_manager.domain.RoomClass;
import com.vaka.hotel_manager.domain.User;
import com.vaka.hotel_manager.service.ReservationService;
import com.vaka.hotel_manager.service.RoomService;
import com.vaka.hotel_manager.service.SecurityService;
import com.vaka.hotel_manager.util.IntegrityUtil;
import com.vaka.hotel_manager.util.ServletToDomainExtractor;
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
    private RoomService roomService;
    private SecurityService securityService;
    private static final Logger LOG = LoggerFactory.getLogger(RoomController.class);

    public void roomsPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User loggedUser = getSecurityService().authenticate(req.getSession());
        LOG.debug("To rooms page");
        req.setAttribute("roomClasses", RoomClass.values());
        req.setAttribute("roomList", getRoomService().findAll(loggedUser));
        req.getRequestDispatcher("/roomsPage.jsp").forward(req, resp);
    }

    public void create(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User loggedUser = getSecurityService().authenticate(req.getSession());
        LOG.debug("Creating room request");
        Room room = ServletToDomainExtractor.extractRoom(req);
        IntegrityUtil.check(room);
        room = getRoomService().create(loggedUser, room);
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
        req.setAttribute("roomClasses", RoomClass.values());
        req.getRequestDispatcher("/roomInfo.jsp").forward(req, resp);

    }


    public void update(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User loggedUser = getSecurityService().authenticate(req.getSession());
        LOG.debug("Updating room");
        Room room = ServletToDomainExtractor.extractRoom(req);
        IntegrityUtil.check(room);
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
