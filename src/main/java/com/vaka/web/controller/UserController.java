package com.vaka.web.controller;

import com.vaka.context.ApplicationContext;
import com.vaka.domain.Reservation;
import com.vaka.domain.Role;
import com.vaka.domain.User;
import com.vaka.service.ReservationService;
import com.vaka.service.SecurityService;
import com.vaka.service.UserService;
import com.vaka.util.DomainExtractor;
import com.vaka.util.DomainUtil;
import com.vaka.util.exception.AuthenticationException;
import com.vaka.util.exception.CreatingException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by Iaroslav on 11/24/2016.
 */
public class UserController {
    private SecurityService securityService;

    private ReservationService reservationService;
    private UserService userService;


    public void signinPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User loggedUser = getSecurityService().authenticate(req, resp);
        if (loggedUser.getRole() != Role.ANONYMOUS) {
            resp.sendRedirect("/");
            return;
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
            } catch (AuthenticationException e) {
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

    public void signUp(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        User loggedUser = getSecurityService().authenticate(req, resp);
        if (loggedUser.getRole() != Role.ANONYMOUS) {
            resp.sendRedirect("/");
            return;
        }
        try {
            User user = DomainExtractor.extractUser(req);
            if (!user.getPassword().equals(req.getParameter("passwordCheck")))
                throw new CreatingException("Passwords should match");
            if (DomainUtil.hasNull(user)) {
                getUserService().create(loggedUser, user);
                getSecurityService().signIn(req, resp, user.getEmail(), req.getParameter("password"));
                resp.sendRedirect("/");
            }
            else {
                resp.setStatus(400);
            }
        } catch (CreatingException ex) {
            req.setAttribute("exception", ex.getMessage());
            req.getRequestDispatcher("/signup.jsp").forward(req, resp);
        }
    }

    public void signOut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User loggedUser = getSecurityService().authenticate(req, resp);
        if (loggedUser.getRole() != Role.ANONYMOUS)
            getSecurityService().logout(req, resp, loggedUser);
        resp.sendRedirect("/");
    }

    public void userPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User loggedUser = getSecurityService().authenticate(req, resp);
        Integer userId = Integer.valueOf(req.getRequestURI().split("/")[2]);
        if (userId.equals(loggedUser.getId())) {
            req.setAttribute("user", loggedUser);
        } else if (loggedUser.getRole() == Role.MANAGER) {
            Optional<User> user = getUserService().getById(loggedUser, userId);
            if (user.isPresent()) {
                req.setAttribute("user", user.get());
            } else {
                resp.sendError(404);
                return;
            }
        } else {
            resp.sendError(401);
            return;
        }
        List<Reservation> list = getReservationService().findActiveByUserId(loggedUser, userId);
        req.setAttribute("loggedUser", loggedUser);
        req.setAttribute("reservationList", list);
        req.getRequestDispatcher("/userInfo.jsp").forward(req, resp);
    }

    public ReservationService getReservationService() {
        if (reservationService == null) {
            synchronized (this) {
                if (reservationService == null) {
                    reservationService = ApplicationContext.getInstance().getBean(ReservationService.class);
                }
            }
        }
        return reservationService;
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
