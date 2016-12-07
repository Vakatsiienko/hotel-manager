package com.vaka.web.servlet;

import com.vaka.context.ApplicationContext;
import com.vaka.domain.Bill;
import com.vaka.util.exception.AuthorizationException;
import com.vaka.web.controller.BillController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Iaroslav on 11/24/2016.
 */
public class BillServlet extends HttpServlet {
    BillController billController;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            if (req.getRequestURI().matches("/bills/[0-9]+") && req.getRequestURI().split("/").length == 3) {
                getBillController().getById(req, resp);
            } else if (req.getRequestURI().equals("/bills/byReservation")) {
                getBillController().getByReservationId(req, resp);
            } else {
                resp.setStatus(404);
                super.doGet(req, resp);
            }
        } catch (AuthorizationException ex) {
            resp.sendError(401, "Unauthorized");
        }
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
