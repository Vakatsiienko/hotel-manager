package com.vaka.web.servlet;

import com.vaka.context.ApplicationContext;
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("/reservations".equals(req.getRequestURI()))
            getReservationController().create(req, resp);
        else resp.setStatus(400);
        if (resp.getStatus() == 400)
            super.doPost(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        if (uri.equals("/reservations/confirmed"))
            getReservationController().confirmedList(req, resp);
        else if (uri.equals("/reservations/requested"))
            getReservationController().requestsList(req, resp);
        else if ("/reservations/applyRoom".equals(req.getRequestURI()))
            getReservationController().applyRoomForReservation(req, resp);
        else if (uri.matches("/reservations/process/[0-9]+"))
            getReservationController().requestProcess(req, resp);
        else if (uri.matches("/reservations/[0-9]+"))
            getReservationController().getById(req, resp);
        else resp.setStatus(400);
        if (resp.getStatus() == 400)
            super.doGet(req, resp);
        System.out.println("GET :" + req.getRequestURI());
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
