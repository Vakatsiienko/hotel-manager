package com.vaka.web.servlet;

import com.vaka.context.ApplicationContext;
import com.vaka.web.controller.UserController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Iaroslav on 11/24/2016.
 */
public class UserServlet extends HttpServlet {
    private UserController userController;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("/users/signin".equals(req.getRequestURI())) {
            getUserController().signinPage(req, resp);
        } else if ("/users/signup".equals(req.getRequestURI())) {
            getUserController().signupPage(req, resp);
        } else if ("users/signout".equals(req.getRequestURI())) {
            getUserController().signOut(req, resp);
        }
        if (resp.getStatus() == 400)
            super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("/users/signin".equals(req.getRequestURI())) {
            getUserController().signIn(req, resp);
        } else if ("/users/signup".equals(req.getRequestURI())) {
            getUserController().signUp(req, resp);
        }
        if (resp.getStatus() == 400)
            super.doGet(req, resp);
        System.out.println("USER GET");
    }

    public UserController getUserController() {
        if (userController == null) {
            synchronized (this) {
                if (userController == null) {
                    userController = ApplicationContext.getInstance().getBean(UserController.class);
                }
            }
        }
        return userController;
    }
}
