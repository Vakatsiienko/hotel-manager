package com.vaka.web.controller;

import com.vaka.context.ApplicationContext;
import com.vaka.domain.Role;
import com.vaka.domain.User;
import com.vaka.service.SecurityService;
import com.vaka.service.UserService;
import com.vaka.util.DomainExtractor;
import com.vaka.util.DomainUtil;
import com.vaka.util.exception.AuthenticateException;
import com.vaka.util.exception.CreatingException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Iaroslav on 11/24/2016.
 */
public class UserController {
    private SecurityService securityService;

    private UserService userService;

    public void signinPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User loggedUser = getSecurityService().authenticate(req, resp);
        if (loggedUser.getRole() != Role.ANONYMOUS) {
            resp.sendRedirect("/");
        }
        req.getRequestDispatcher("/signin.jsp").forward(req, resp);
    }

    public void signIn(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User loggedUser = getSecurityService().authenticate(req, resp);
        if (loggedUser.getRole() != Role.ANONYMOUS) {
            req.setAttribute("loggedUser", loggedUser);
            req.getRequestDispatcher("/").forward(req, resp);
        } else {
            String email = req.getParameter("email");
            String password = req.getParameter("password");
            try {
                getSecurityService().signIn(req, resp, email, password);
            } catch (AuthenticateException e) {
                req.setAttribute("exception", e.getMessage());
                req.getRequestDispatcher("/signin.jsp").forward(req, resp);
                return;
            }
            resp.sendRedirect("/");
        }

    }


    public void signupPage(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        User loggedUser = getSecurityService().authenticate(req, resp);
        if (loggedUser.getRole() == Role.ANONYMOUS)
            req.getRequestDispatcher("/signup.jsp").forward(req, resp);
        else
            resp.sendRedirect("/");
    }

    public void signUp(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User loggedUser = getSecurityService().authenticate(req, resp);
        if (loggedUser.getRole() != Role.ANONYMOUS) {
            resp.sendRedirect("/");
            return;
        }
        try {
            User user = DomainExtractor.extractUser(req);
            if (!user.getPassword().equals(req.getParameter("passwordCheck")))
                throw new CreatingException("Passwords should match");
            if (DomainUtil.hasCrucialNulls(user))
                getUserService().create(loggedUser, user);
            else {
                resp.setStatus(400);
                return;
            }

        } catch (CreatingException ex) {
            req.setAttribute("exception", ex.getMessage());
        }
        resp.sendRedirect("/");
    }

    public void signOut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User loggedUser = getSecurityService().authenticate(req, resp);
        if (loggedUser.getRole() != Role.ANONYMOUS)
            getSecurityService().logout(req, resp, loggedUser);
        resp.sendRedirect("/");
    }

    private UserService getUserService() {
        if (userService == null) {
            synchronized (this) {
                if (userService == null) {
                    userService = ApplicationContext.getInstance().getBean(UserService.class);
                }
            }
        }
        return userService;
    }

    private SecurityService getSecurityService() {
        if (securityService == null) {
            synchronized (this) {
                if (securityService == null) {
                    securityService = ApplicationContext.getInstance().getBean(SecurityService.class);
                }
            }
        }
        return securityService;
    }
}
