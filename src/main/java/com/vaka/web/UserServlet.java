package com.vaka.web;

import com.vaka.util.ApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Iaroslav on 11/24/2016.
 */
public class UserServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        super.init();
        ApplicationContext context = ApplicationContext.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("/authenticate".equals(req.getRequestURI())) {

        } else if ("/registration".equals(req.getRequestURI())){

        }

        super.doPost(req, resp);
    }
}
