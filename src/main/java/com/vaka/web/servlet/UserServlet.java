package com.vaka.web.servlet;

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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("/login".equals(req.getRequestURI())){
            //login.jsp
        }
        else if ("/login/createToken".equals(req.getRequestURI())) {
            //get credentials and set token

        } else if ("/login/register".equals(req.getRequestURI())) {
            //add new user and set token
        }
        System.out.println("USER GET");

    }
}
