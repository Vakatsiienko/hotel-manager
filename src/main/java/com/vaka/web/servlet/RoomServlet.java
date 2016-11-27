package com.vaka.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Iaroslav on 11/24/2016.
 */
public class RoomServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("/rooms".equals(req.getRequestURI())) {
            //return list of rooms
        } else if (req.getRequestURI().startsWith("/rooms/findForRequest?")) {
            
            //return list of suitable rooms
        }
        super.doGet(req, resp);
    }
}
