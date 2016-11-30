package com.vaka.web.servlet;

import com.vaka.domain.RoomClass;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Iaroslav on 11/29/2016.
 */
public class MainServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("/".equals(req.getRequestURI())) {
            req.setAttribute("roomClazzez", RoomClass.values());
            req.getRequestDispatcher("/index2.jsp").forward(req, resp);
        }
        System.out.println("Main");
    }
}
