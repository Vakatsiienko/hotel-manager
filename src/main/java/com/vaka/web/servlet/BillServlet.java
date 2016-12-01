package com.vaka.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Iaroslav on 11/24/2016.
 */
public class BillServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("/payments".equals(req.getRequestURI())) {
            //return all unpaid payments
        } else if (req.getRequestURI().startsWith("/payments?userId=")) {
            //return all unpaid payments by specific user
        }
        System.out.println("BILL GET");
        super.doGet(req, resp);
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("/payments/pay".equals(req.getRequestURI())) {
            //return paid bill
        }
        System.out.println("BILL POST");

        super.doPost(req, resp);
    }
}
