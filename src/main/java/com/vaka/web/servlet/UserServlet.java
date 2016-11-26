package com.vaka.web.servlet;

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
        ApplicationContext.init();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("/login".equals(req.getRequestURI())){
            //login.jsp
        }
        else if ("/login/authenticate".equals(req.getRequestURI())) {
            //get credentials and set token

        } else if ("/login/register".equals(req.getRequestURI())) {
            //add new user and set token
        }

        super.doPost(req, resp);
    }
}
