package com.vaka.web.servlet;

import com.vaka.context.ApplicationContext;
import com.vaka.domain.Reservation;
import com.vaka.util.exception.AuthorizationException;
import com.vaka.web.controller.ReservationController;
import com.vaka.web.controller.UserController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Iaroslav on 11/24/2016.
 */
public class UserServlet extends HttpServlet {
    private UserController userController;
    private ReservationController reservationController;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
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
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("/users/signin".equals(req.getRequestURI())) {
            getUserController().signIn(req, resp);
        } else if ("/users/signup".equals(req.getRequestURI())) {
            getUserController().signUp(req, resp);
        } else resp.sendError(404, "Not Found");
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
}
