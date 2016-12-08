package com.vaka.hotel_manager.web.controller;

import com.vaka.hotel_manager.context.ApplicationContext;
import com.vaka.hotel_manager.domain.Reservation;
import com.vaka.hotel_manager.domain.Role;
import com.vaka.hotel_manager.domain.RoomClass;
import com.vaka.hotel_manager.domain.User;
import com.vaka.hotel_manager.service.ReservationService;
import com.vaka.hotel_manager.service.SecurityService;
import com.vaka.hotel_manager.service.UserService;
import com.vaka.hotel_manager.util.DomainExtractor;
import com.vaka.hotel_manager.util.DomainUtil;
import com.vaka.hotel_manager.util.exception.AuthenticationException;
import com.vaka.hotel_manager.util.exception.CreatingException;

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

    public void toRoot(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = getSecurityService().authenticate(req, resp);
        req.setAttribute("loggedUser", user);
        req.setAttribute("roomClasses", RoomClass.values());
        req.getRequestDispatcher("/home.jsp").forward(req, resp);
    }

    public void signupPage(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        User loggedUser = getSecurityService().authenticate(req, resp);
        if (loggedUser.getRole() == Role.ANONYMOUS)
            req.getRequestDispatcher("/signup.jsp").forward(req, resp);
        else
            resp.sendRedirect("/");
    }
//TODO hash password bcrypt
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
            } else {
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
            //if loggedUser isn't looking user - get him from bd
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
