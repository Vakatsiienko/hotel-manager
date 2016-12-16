package com.vaka.hotel_manager.web.servlet;

import com.vaka.hotel_manager.context.ApplicationContext;
import com.vaka.hotel_manager.util.exception.ApplicationException;
import com.vaka.hotel_manager.util.exception.AuthorizationException;
import com.vaka.hotel_manager.util.exception.NotFoundException;
import com.vaka.hotel_manager.web.controller.BillController;
import com.vaka.hotel_manager.web.controller.ReservationController;
import com.vaka.hotel_manager.web.controller.RoomController;
import com.vaka.hotel_manager.web.controller.UserController;
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


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String uri = req.getRequestURI();
            LOG.debug("GET request uri {}", uri);
            if ("/".equals(uri)) {
                getUserController().toRoot(req, resp);
            } else if ("/signin".equals(uri)) {
                getUserController().signinPage(req, resp);
            } else if ("/signup".equals(uri)) {
                getUserController().signupPage(req, resp);
            } else if ("/signout".equals(uri)) {
                getUserController().signOut(req, resp);
            } else if (uri.startsWith("/users")) {
                if (uri.matches("/users/[0-9]+")) {
                    getUserController().userPage(req, resp);
                } else resp.sendError(404, "Not Found");
            } else if (uri.startsWith("/reservations")) {
                if (uri.equals("/reservations/confirmed")) {
                    getReservationController().confirmedList(req, resp);
                } else if (uri.equals("/reservations/requested")) {
                    getReservationController().requestsList(req, resp);
                } else if (uri.equals("/reservations/applyRoom")) {
                    getReservationController().applyRoomForReservation(req, resp);
                } else if (uri.matches("/reservations/process/[0-9]+")) {
                    getReservationController().processingPage(req, resp);
                } else if (uri.matches("/reservations/[0-9]+")) {
                    getReservationController().getById(req, resp);
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
                    roomController.roomPage(req, resp);
                }
            } else
                resp.sendError(404, "Not Found");
        } catch (NotFoundException e1) {
            LOG.debug(e1.getMessage(), e1);
            resp.sendError(404, e1.getMessage());
        } catch (IllegalArgumentException e2) {
            LOG.debug(e2.getMessage(), e2);
            resp.sendError(400, e2.getMessage());
        } catch (AuthorizationException e3) {
            LOG.debug(e3.getMessage(), e3);
            resp.sendError(403, e3.getMessage());
        } catch (ApplicationException e4) {
            LOG.error(e4.getMessage(), e4);
            resp.sendError(500, e4.getMessage());
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
            } else if ("/reservations".equals(strUri)) {
                getReservationController().create(req, resp);
            } else if (strUri.matches("/reservations/[0-9]+/reject")) {
                getReservationController().reject(req, resp);
            } else if (strUri.equals("/rooms")) {
                getRoomController().create(req, resp);
            } else if (strUri.matches("/rooms/[0-9]+")) {
                String method = req.getParameter("method");
                if (method.equals("POST")) {
                    getRoomController().update(req, resp);
                } else if (method.equals("DELETE")) {
                    getRoomController().delete(req, resp);
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
            resp.sendError(401, e3.getMessage());
        } catch (ApplicationException e4) {
            LOG.error(e4.getMessage(), e4);
            resp.sendError(500, e4.getMessage());
        }
    }

    public RoomController getRoomController() {
        if (roomController == null) {
            synchronized (this) {
                if (roomController == null) {
                    roomController = ApplicationContext.getInstance().getBean(RoomController.class);
                }
            }
        }
        return roomController;
    }

    public UserController getUserController() {
        if (userController == null) {
            synchronized (this) {
                if (userController == null) {
                    userController = ApplicationContext.getInstance().getBean(UserController.class);
                }
            }
        }
        return userController;
    }

    public ReservationController getReservationController() {
        if (reservationController == null) {
            synchronized (this) {
                if (reservationController == null) {
                    reservationController = ApplicationContext.getInstance().getBean(ReservationController.class);
                }
            }
        }
        return reservationController;
    }

    public BillController getBillController() {
        if (billController == null) {
            synchronized (this) {
                if (billController == null) {
                    billController = ApplicationContext.getInstance().getBean(BillController.class);
                }
            }
        }
        return billController;
    }
}
