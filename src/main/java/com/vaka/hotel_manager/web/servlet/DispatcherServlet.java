package com.vaka.hotel_manager.web.servlet;

import com.vaka.hotel_manager.context.ApplicationContext;
import com.vaka.hotel_manager.util.exception.ApplicationException;
import com.vaka.hotel_manager.util.exception.AuthorizationException;
import com.vaka.hotel_manager.util.exception.NotFoundException;
import com.vaka.hotel_manager.web.controller.BillController;
import com.vaka.hotel_manager.web.controller.ReservationController;
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


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String uri = req.getRequestURI();
            HttpServletRequestWrapper request = new HttpServletRequestWrapper(req);
            if ("/".equals(req.getRequestURI())) {
                getUserController().toRoot(req, resp);
            } else if (uri.startsWith("/users")) {
                if ("/users/signin".equals(uri)) {
                    getUserController().signinPage(req, resp);
                } else if ("/users/signup".equals(uri)) {
                    getUserController().signupPage(req, resp);
                } else if ("/users/signout".equals(uri)) {
                    getUserController().signOut(req, resp);
                } else if (uri.matches("/users/[0-9]+/reservations") &&
                        uri.split("/").length == 3) {
                    getReservationController().getByUser(req, resp);
                } else if (uri.matches("/users/[0-9]+") &&
                        uri.split("/").length == 3) {
                    getUserController().userPage(req, resp);
                } else resp.sendError(404, "Not Found");
            } else if (uri.startsWith("/reservations")) {
                if (uri.equals("/reservations/confirmed")) {
                    getReservationController().confirmedList(req, resp);
                } else if (uri.equals("/reservations/requested")) {
                    getReservationController().requestsList(req, resp);
                } else if (req.getRequestURI().equals("/reservations/applyRoom")) {
                    getReservationController().applyRoomForReservation(req, resp);
                } else if (uri.matches("/reservations/process/[0-9]+") && uri.split("/").length == 4) {
                    getReservationController().requestProcess(req, resp);
                } else if (uri.matches("/reservations/[0-9]+") && uri.split("/").length == 3) {
                    getReservationController().getById(req, resp);
                } else if (uri.equals("/reservations/reject")) {
                    getReservationController().reject(req, resp);
                } else {
                    resp.sendError(404, "Not Found");
                }
            } else if (uri.startsWith("/bills")) {
                if (req.getRequestURI().matches("/bills/[0-9]+") && req.getRequestURI().split("/").length == 3) {
                    getBillController().getById(req, resp);
                } else if (req.getRequestURI().equals("/bills/byReservation")) {
                    getBillController().getByReservationId(req, resp);
                } else {
                    resp.sendError(404);
                }
            }
        } catch (NotFoundException e1) {
            resp.sendError(404, e1.getMessage());
        } catch (AuthorizationException e) {
            resp.sendError(401, e.getMessage());
        } catch (ApplicationException ex) {
            LOG.error(ex.getMessage(), ex);
            resp.sendError(500, ex.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {

            if ("/users/signin".equals(req.getRequestURI())) {
                getUserController().signIn(req, resp);
            } else if ("/users/signup".equals(req.getRequestURI())) {
                getUserController().signUp(req, resp);
            } else resp.sendError(404, "Not Found");
            if ("/reservations".equals(req.getRequestURI())) {
                getReservationController().create(req, resp);
            } else {
                resp.sendError(404, "Not Found");
                super.doPost(req, resp);
            }
        } catch (AuthorizationException ex) {
            resp.sendError(401, "Unauthorized");
        }
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
