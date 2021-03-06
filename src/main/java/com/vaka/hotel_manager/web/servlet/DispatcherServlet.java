package com.vaka.hotel_manager.web.servlet;

import com.vaka.hotel_manager.core.context.ApplicationContextHolder;
import com.vaka.hotel_manager.util.exception.AuthorizationException;
import com.vaka.hotel_manager.util.exception.NotFoundException;
import com.vaka.hotel_manager.web.controller.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Iaroslav on 12/8/2016.
 */
public class DispatcherServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(DispatcherServlet.class);
    private UserController userController;
    private ReservationController reservationController;
    private BillController billController;
    private RoomController roomController;
    private RoomClassController roomClassController;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String uri = req.getRequestURI();
            LOG.debug("GET request uri {}", uri);
            if ("/".equals(uri)) {
                getUserController().toRoot(req, resp);
            } else if (uri.startsWith("/sign")) {
                if ("/signin".equals(uri)) {
                    getUserController().signinPage(req, resp);
                } else if ("/signup".equals(uri)) {
                    getUserController().signupPage(req, resp);
                } else if (uri.equals("/signup-vk")) {
                    getUserController().signInVk(req, resp);
                } else resp.sendError(404, "Not Found");
            } else if (uri.startsWith("/users")) {
                if (uri.matches("/users/[0-9]+")) {
                    getUserController().userPage(req, resp);
                } else resp.sendError(404, "Not Found");
            } else if (uri.startsWith("/reservations")) {
                if (uri.equals("/reservations/confirmed")) {
                    getReservationController().confirmedPage(req, resp);
                } else if (uri.equals("/reservations/requested")) {
                    getReservationController().requestsList(req, resp);
                } else if (uri.equals("/reservations/applyRoom")) {
                    getReservationController().applyRoomForReservation(req, resp);
                } else if (uri.matches("/reservations/process/[0-9]+")) {
                    getReservationController().processingPage(req, resp);
                } else if (uri.matches("/reservations/[0-9]+")) {
                    getReservationController().getById(req, resp);
                } else if (uri.equals("/reservations/confirmed/show-arrival")) {
                    getReservationController().showArrival(req, resp);
                } else {
                    resp.sendError(404, "Not Found");
                }
            } else if (uri.startsWith("/bills")) {
                if (uri.matches("/bills/[0-9]+")) {
                    getBillController().getById(req, resp);
                } else if (uri.equals("/bills/byReservation")) {
                    getBillController().getByReservationId(req, resp);
                } else {
                    resp.sendError(404);
                }
            } else if (uri.startsWith("/rooms")) {
                if (uri.equals("/rooms")) {
                    getRoomController().roomsPage(req, resp);
                } else if (uri.matches("/rooms/[0-9]+")) {
                    getRoomController().roomPage(req, resp);
                }
            } else if (uri.equals("/room-classes")) {
                getRoomClassController().page(req, resp);
            } else {
                resp.sendError(404, "Not Found");
            }
        } catch (NotFoundException e1) {
            LOG.debug(e1.getMessage(), e1);
            resp.sendError(404, e1.getMessage());
        } catch (IllegalArgumentException e2) {
            LOG.debug(e2.getMessage(), e2);
            resp.sendError(400, e2.getMessage());
        } catch (AuthorizationException e3) {
            LOG.debug(e3.getMessage(), e3);
            resp.sendError(403, e3.getMessage());
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            resp.sendError(500, "Internal server error");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String strUri = req.getRequestURI();
            LOG.debug("POST request uri {}", strUri);
            if ("/signin".equals(strUri)) {
                getUserController().signIn(req, resp);
            } else if ("/signup".equals(strUri)) {
                getUserController().signUp(req, resp);
            } else if ("/signup-vk".equals(strUri)) {
                getUserController().signUpVk(req, resp);
            } else if ("/signout".equals(strUri)) {
                getUserController().signOut(req, resp);
            } else if ("/reservations".equals(strUri)) {
                getReservationController().create(req, resp);
            } else if (strUri.matches("/reservations/[0-9]+/reject")) {
                getReservationController().reject(req, resp);
            } else if (strUri.startsWith("/rooms")) {
                if ("/rooms".equals(strUri)) {
                    getRoomController().create(req, resp);
                } else if (strUri.matches("/rooms/[0-9]+")) {
                    String method = req.getParameter("method");
                    if ("PUT".equals(method)) {
                        getRoomController().update(req, resp);
                    } else if ("DELETE".equals(method)) {
                        getRoomController().delete(req, resp);
                    } else resp.sendError(404, "Not Found");
                } else {
                    resp.sendError(404, "Not Found");
                }
            } else if (strUri.startsWith("/room-classes")) {
                if (strUri.equals("/room-classes"))
                    getRoomClassController().create(req, resp);
                else if (strUri.matches("/room-classes/[0-9]+")) {
                    String method = req.getParameter("method");
                    if ("PUT".equals(method))
                        getRoomClassController().update(req, resp);
                    else if ("DELETE".equals(method))
                        getRoomClassController().delete(req, resp);
                    else resp.sendError(404, "Not Found");
                } else resp.sendError(404, "Not Found");
            } else {
                resp.sendError(404, "Not Found");
            }
        } catch (NotFoundException e1) {
            LOG.debug(e1.getMessage(), e1);
            resp.sendError(404, e1.getMessage());
        } catch (IllegalArgumentException e2) {
            LOG.debug(e2.getMessage(), e2);
            resp.sendError(400, e2.getMessage());
        } catch (AuthorizationException e3) {
            LOG.debug(e3.getMessage(), e3);
            resp.sendError(403, e3.getMessage());
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            resp.sendError(500, "Internal server error");
        }
    }

    public RoomController getRoomController() {
        if (roomController == null) {
            roomController = ApplicationContextHolder.getContext().getBean(RoomController.class);
        }
        return roomController;
    }

    public UserController getUserController() {
        if (userController == null) {
            userController = ApplicationContextHolder.getContext().getBean(UserController.class);
        }
        return userController;
    }

    public ReservationController getReservationController() {
        if (reservationController == null) {
            reservationController = ApplicationContextHolder.getContext().getBean(ReservationController.class);
        }
        return reservationController;
    }

    public BillController getBillController() {
        if (billController == null) {
            billController = ApplicationContextHolder.getContext().getBean(BillController.class);
        }
        return billController;
    }

    public RoomClassController getRoomClassController() {
        if (roomClassController == null) {
            roomClassController = ApplicationContextHolder.getContext().getBean(RoomClassController.class);
        }
        return roomClassController;
    }
}
