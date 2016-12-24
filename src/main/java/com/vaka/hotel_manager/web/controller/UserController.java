package com.vaka.hotel_manager.web.controller;

import com.vaka.hotel_manager.context.ApplicationContext;
import com.vaka.hotel_manager.domain.DTO.ReservationDTO;
import com.vaka.hotel_manager.domain.RoomClass;
import com.vaka.hotel_manager.domain.User;
import com.vaka.hotel_manager.service.ReservationService;
import com.vaka.hotel_manager.service.SecurityService;
import com.vaka.hotel_manager.service.UserService;
import com.vaka.hotel_manager.util.IntegrityUtil;
import com.vaka.hotel_manager.util.ServletToDomainExtractor;
import com.vaka.hotel_manager.util.exception.AuthenticationException;
import com.vaka.hotel_manager.util.exception.CreatingException;
import com.vaka.hotel_manager.util.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;

/**
 * Created by Iaroslav on 11/24/2016.
 */
public class UserController {
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    private SecurityService securityService;

    private ReservationService reservationService;
    private UserService userService;


    public void signinPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOG.debug("To signin page");
        req.getRequestDispatcher("/signin.jsp").forward(req, resp);
    }

    public void signIn(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOG.debug("Sign in request");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        if (email == null || password == null)
            throw new IllegalArgumentException("Email or/and password missing");
        try {
            LOG.debug("signin user with email: {}", email);
            getSecurityService().signIn(req.getSession(), email, password);
        } catch (AuthenticationException e) {
            LOG.debug(e.getMessage(), e);
            req.setAttribute("exception", e.getMessage());
            req.getRequestDispatcher("/signin.jsp").forward(req, resp);
            return;
        }
        String redirectUri = req.getParameter("redirectUri");
        if (redirectUri == null){
            resp.sendRedirect("/");
            return;
        }
        //redirect to redirecting url and filter sensitive params
        StringJoiner params = new StringJoiner("&");
        req.getParameterMap().entrySet().stream()
                .filter(entry -> !entry.getKey().equals("email")
                        && !entry.getKey().equals("password"))
                .forEach(entry -> params.add(String.join("=", entry.getKey(), entry.getValue()[0])));
        redirectUri = String.join("?", redirectUri, params.toString());
        resp.sendRedirect(redirectUri);
    }

    public void toRoot(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getSecurityService().authenticate(req.getSession());
        LOG.debug("Return home page");
        req.setAttribute("roomClasses", RoomClass.values());
        req.getRequestDispatcher("/home.jsp").forward(req, resp);
    }

    public void signupPage(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        LOG.debug("Sign up page request");
        LOG.debug("Return signup page");
        req.getRequestDispatcher("/signup.jsp").forward(req, resp);
    }

    /**
     * Getting user from req and checking fields, then creating user and signin him
     */
    public void signUp(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        User loggedUser = getSecurityService().authenticate(req.getSession());
        LOG.debug("Sign up request");
        try {
            User user = ServletToDomainExtractor.extractCustomer(req);
            if (!user.getPassword().equals(req.getParameter("passwordCheck")))
                throw new CreatingException("PasswordCheckException");
            IntegrityUtil.check(user);
            getUserService().create(loggedUser, user);
            getSecurityService().signIn(req.getSession(), user.getEmail(), req.getParameter("password"));
            resp.sendRedirect("/");
        } catch (CreatingException ex) {
            LOG.debug(ex.getMessage(), ex);
            req.setAttribute("exception", ex.getMessage());
            req.getRequestDispatcher("/signup.jsp").forward(req, resp);
        }
    }

    public void signOut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOG.debug("Sign out request");
        getSecurityService().logout(req.getSession());
        resp.sendRedirect("/");
    }

    public void userPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User loggedUser = getSecurityService().authenticate(req.getSession());
        Integer userId = Integer.valueOf(req.getRequestURI().split("/")[2]);
        LOG.debug("User page request, userId: {}", userId, loggedUser);
        Optional<User> user = getUserService().getById(loggedUser, userId);
        if (user.isPresent()) {
            req.setAttribute("user", user.get());
        } else {
            throw new NotFoundException("User not found");
        }
        List<ReservationDTO> list = getReservationService().findActiveByUserId(loggedUser, userId);

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
