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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("/requests".equals(req.getRequestURI())) {
            //return list of requests
        } else if (req.getRequestURI().startsWith("/requests?id")) {
            //return request detail info
        }
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("requests/add".equals(req.getRequestURI())) {
            //return
        }
        super.doPost(req, resp);
    }
}
