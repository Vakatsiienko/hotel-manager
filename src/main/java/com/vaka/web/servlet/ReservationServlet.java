package com.vaka.web.servlet;

import com.vaka.context.ApplicationContext;
import com.vaka.util.exception.AuthorizationException;
import com.vaka.web.controller.ReservationController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Iaroslav on 12/1/2016.
 */
public class ReservationServlet extends HttpServlet {
    private ReservationController reservationController;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
        String uri = req.getRequestURI();
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
        } catch (AuthorizationException ex) {
            resp.sendError(401, "Unauthorized");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            if ("/reservations".equals(req.getRequestURI())) {
                getReservationController().create(req, resp);
            } else {
                resp.sendError(404, "Not Found");
                super.doPost(req, resp);
            }
        } catch (AuthorizationException ex) {
            resp.sendError(401,"Unauthorized");
        }
    }


    private ReservationController getReservationController() {
        if (reservationController == null)
            synchronized (this) {
                if (reservationController == null) {
                    reservationController = ApplicationContext.getInstance().getBean(ReservationController.class);
                }
            }
        return reservationController;
    }

}
