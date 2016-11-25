package com.vaka.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Iaroslav on 11/24/2016.
 */
public class ReservationRequestServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("/reservationRequest".equals(req.getRequestURI())) {

        } else if ("/requests".equals(req.getRequestURI())) {

        }
            super.doPost(req, resp);
    }
}
