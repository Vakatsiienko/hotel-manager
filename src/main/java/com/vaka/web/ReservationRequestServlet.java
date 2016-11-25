package com.vaka.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaka.domain.ReservationRequest;
import com.vaka.util.ApplicationContext;
import com.vaka.web.controller.ReservationRequestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

/**
 * Created by Iaroslav on 11/24/2016.
 */
public class ReservationRequestServlet extends HttpServlet {
    private ReservationRequestController requestController;

    private static final String JSON_BAD_REQUEST = "{\"Error\" : \"Bad Request\"}";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        if ("/requests".equals(req.getRequestURI())) {
            String jsonList = objectMapper.writeValueAsString(getRequestController().list());
            writer.write(jsonList);
            writer.flush();

            //return list of requests
        } else if (req.getRequestURI().startsWith("/requests?id=")) {
            Integer id = Integer.parseInt(req.getParameter("id"));
            String jsonResp = objectMapper.writeValueAsString(getRequestController().getById(id));
            writer.write(jsonResp);
            writer.flush();
            //return request detail info
        }
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        if ("requests/add".equals(req.getRequestURI())) {
            String body = req.getReader().lines()
                    .collect(Collectors.joining(System.lineSeparator()));
            ReservationRequest request = objectMapper.readValue(body, ReservationRequest.class);
            if (request.isNew()) {
                String jsonResp = objectMapper.writeValueAsString(getRequestController().create(request));
                writer.write(jsonResp);
                writer.flush();
            } else {
                writer.write(JSON_BAD_REQUEST);
                writer.flush();
            }
            //return
        }
        super.doPost(req, resp);
    }

    public ReservationRequestController getRequestController() {
        if (requestController == null)
            synchronized (ReservationRequestServlet.class){
                if (requestController == null) {
                    requestController = ApplicationContext.getBean(ReservationRequestController.class);
                }
            }
        return requestController;
    }
}
