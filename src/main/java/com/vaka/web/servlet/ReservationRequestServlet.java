package com.vaka.web.servlet;

import com.vaka.domain.ReservationRequest;
import com.vaka.domain.Manager;
import com.vaka.service.SecurityService;
import com.vaka.util.ApplicationContext;
import com.vaka.util.ServletDomainExtractor;
import com.vaka.web.controller.ReservationRequestController;
import com.vaka.web.controller.RoomController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Iaroslav on 11/24/2016.
 */
public class ReservationRequestServlet extends HttpServlet {

    private RoomController roomController;
    private ReservationRequestController requestController;
    private SecurityService securityService;//will be moved to controller with all parsing operations

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Manager loggedUser = new Manager();
        if ("/requests".equals(req.getRequestURI())) {
            req.setAttribute("requestList", getReservationRequestController().list(loggedUser));
            req.getRequestDispatcher("requests.jsp").forward(req, resp);
            //return list of requests
        } else if (req.getRequestURI().matches("/requests/process\\?id=[0-9]+")) {
            Integer reqId = Integer.parseInt(req.getParameter("id"));
            req.setAttribute("requestInfo", getReservationRequestController().getById(reqId));
            req.setAttribute("rooms", getRoomController().getForRequestById(reqId));
            req.getRequestDispatcher("requestProcessing.jsp").forward(req, resp);
            //return request detail info, rooms on processing jsp
        } else {
            resp.setStatus(400);
        }
//        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("/requests".equals(req.getRequestURI())) {
            ReservationRequest reservationRequest = ServletDomainExtractor.extractReservationRequest(req);
            if (reservationRequest.isNew()) {
                getReservationRequestController().create(reservationRequest);
                resp.sendRedirect("/requests");
            } else resp.setStatus(400);
        } else if (req.getRequestURI().matches("/requests/process\\?id=[0-9]+")){
            Integer reqId = Integer.parseInt(req.getParameter("id"));
            ReservationRequest reservationRequest = ServletDomainExtractor.extractReservationRequest(req);
            req.setAttribute("requestInfo", getReservationRequestController().getById(reqId));
            req.setAttribute("rooms", getRoomController().getForRequest(reservationRequest));
            req.getRequestDispatcher("requestProcessing.jsp").forward(req, resp);
        }
        else {
            resp.setStatus(400);
        }
//        super.doPost(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        Manager loggedUser = getSecurityService().authenticate(req, resp);
//        if (req.getRequestURI().matches("/requests/[0-9]+")) {
//            Integer id = Integer.parseInt(req.getRequestURI().split("/requests/")[1]);
//            getReservationRequestController().delete( id);
//            resp.sendRedirect("requests");
//        } else {
//            resp.setStatus(400);
//        }
        super.doDelete(req, resp);
    }

    public ReservationRequestController getReservationRequestController() {
        if (requestController == null)
            synchronized (this) {
                if (requestController == null) {
                    requestController = ApplicationContext.getBean(ReservationRequestController.class);
                }
            }
        return requestController;
    }

    public RoomController getRoomController() {
        if (roomController == null)
            synchronized (this) {
                if (roomController == null) {
                    roomController = ApplicationContext.getBean(RoomController.class);
                }
            }
        return roomController;
    }

    public SecurityService getSecurityService() {
        if (securityService == null) {
            synchronized (this) {
                if (securityService == null) {
                    securityService = ApplicationContext.getBean(SecurityService.class);
                }
            }
        }
        return securityService;
    }
}
