package com.vaka.web.controller;

import com.vaka.context.ApplicationContext;
import com.vaka.domain.Bill;
import com.vaka.domain.User;
import com.vaka.repository.SecurityRepository;
import com.vaka.service.BillService;
import com.vaka.service.SecurityService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * Created by Iaroslav on 11/25/2016.
 */
public class BillController {
    private BillService billService;
    private SecurityService securityService;

    public void getById(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User loggedUser = getSecurityService().authenticate(req, resp);
        String strId = req.getRequestURI().split("/")[2];
        Integer id = Integer.valueOf(strId);
        Optional<Bill> bill = getBillService().getById(loggedUser, id);
        if (bill.isPresent()) {
            req.setAttribute("bill", bill.get());
            req.setAttribute("loggedUser", loggedUser);
            req.getRequestDispatcher("/billInfo.jsp").forward(req, resp);
        }
        else resp.sendError(404);

    }

    public void getByReservationId(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User loggedUser = getSecurityService().authenticate(req, resp);
        Integer id = Integer.valueOf(req.getParameter("id"));
        req.setAttribute("loggedUser", loggedUser);
        req.setAttribute("bill", getBillService().getBillByReservationId(loggedUser, id).get());
        req.getRequestDispatcher("/billInfo.jsp").forward(req, resp);
    }

    public SecurityService getSecurityService() {
        if (securityService == null) {
            synchronized (this) {
                if (securityService == null) {
                    securityService = ApplicationContext.getInstance().getBean(SecurityService.class);
                }
            }
        }
        return securityService;
    }

    public BillService getBillService() {
        if (billService == null) {
            synchronized (this) {
                if (billService == null) {
                    billService = ApplicationContext.getInstance().getBean(BillService.class);
                }
            }
        }
        return billService;
    }
}
