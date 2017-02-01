package com.vaka.hotel_manager.web.controller;

import com.vaka.hotel_manager.core.context.ApplicationContext;
import com.vaka.hotel_manager.core.security.SecurityService;
import com.vaka.hotel_manager.domain.DTO.ReservationDTO;
import com.vaka.hotel_manager.domain.User;
import com.vaka.hotel_manager.service.ReservationService;
import com.vaka.hotel_manager.service.RoomClassService;
import com.vaka.hotel_manager.service.UserService;
import com.vaka.hotel_manager.util.ServletExtractor;
import com.vaka.hotel_manager.util.ValidationUtil;
import com.vaka.hotel_manager.util.exception.AuthenticationException;
import com.vaka.hotel_manager.util.exception.CreatingException;
import com.vaka.hotel_manager.util.exception.NotFoundException;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by Iaroslav on 11/24/2016.
 */
public class UserController {
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    private SecurityService securityService;

    private ReservationService reservationService;
    private UserService userService;
    private RoomClassService roomClassService;

    public void signUpVk(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User loggedUser = getSecurityService().authenticate(req.getSession());
        LOG.debug("Sign up vk");
        User vkUser = (User) req.getSession().getAttribute("vkUser");
        if (vkUser == null) {
            throw new IllegalStateException("");
        }
        String phone = req.getParameter("phoneNumber");
        String name = req.getParameter("name");
        vkUser.setPhoneNumber(phone);
        vkUser.setName(name);
        vkUser.setPassword(RandomStringUtils.random(8));
        try {
            loggedUser = getUserService().create(loggedUser, vkUser);
        } catch (CreatingException e) {
            LOG.debug(e.getMessage(), e);
            req.setAttribute("exception", e.getMessage());
            req.getRequestDispatcher("/jsp/signin.jsp").forward(req, resp);
            return;
        }
        req.getSession().setAttribute("loggedUser", loggedUser);
        resp.sendRedirect("/");
    }

    public void signInVk(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        LOG.debug("Sign in vk");
        String code = req.getParameter("code");
        if (code == null) {
            String error = req.getParameter("error");
            String errorDescription = req.getParameter("error_description");
            resp.sendError(400, String.format("Error: %s, error description: %s", error, errorDescription));
            return;
        }
        try {
            if (getSecurityService().signInVk(session, code)) {
                User loggedUser = (User) session.getAttribute("loggedUser");
                resp.sendRedirect(String.format("/users/%s", loggedUser.getId()));
            } else req.getRequestDispatcher("/jsp/vkSignUp.jsp").forward(req, resp);
        } catch (CreatingException ex) {
            LOG.debug(ex.getMessage(), ex);
            req.setAttribute("exception", ex.getMessage());
            req.getRequestDispatcher("/jsp/signin.jsp").forward(req, resp);
        }
    }

    public void signinPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOG.debug("To signin page");
        req.getRequestDispatcher("/jsp/signin.jsp").forward(req, resp);
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
            req.getRequestDispatcher("/jsp/signin.jsp").forward(req, resp);
            return;
        }
        String redirectUri = req.getParameter("redirectUri");
        if (redirectUri == null){
            resp.sendRedirect("/users/" + ((User) req.getSession().getAttribute("loggedUser")).getId());
            return;
        }
        resp.sendRedirect(redirectUri);
    }

    public void toRoot(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User loggedUser = getSecurityService().authenticate(req.getSession());
        LOG.debug("Return home page");
        req.setAttribute("roomClasses", getRoomClassService().findAll(loggedUser));
        req.getRequestDispatcher("/jsp/home.jsp").forward(req, resp);
    }

    public void signupPage(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        LOG.debug("Sign up page request");
        LOG.debug("Return signup page");
        req.getRequestDispatcher("/jsp/signup.jsp").forward(req, resp);
    }

    /**
     * Getting user from req and checking fields, then creating user and signin him
     */
    public void signUp(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        User loggedUser = getSecurityService().authenticate(req.getSession());
        LOG.debug("Sign up request");
        User user = ServletExtractor.extractCustomer(req);
        try {
            if (!user.getPassword().equals(req.getParameter("passwordCheck")))
                throw new CreatingException("PasswordCheckException");
            ValidationUtil.validate(user);
            getUserService().create(loggedUser, user);
            getSecurityService().signIn(req.getSession(), user.getEmail(), req.getParameter("password"));
            resp.sendRedirect("/");
        } catch (CreatingException ex) {
            LOG.debug(ex.getMessage(), ex);
            req.getSession().setAttribute("email", user.getEmail());
            req.getSession().setAttribute("exception", ex.getMessage());
            resp.sendRedirect("/signin");
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
        req.getRequestDispatcher("/jsp/userInfo.jsp").forward(req, resp);
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

    public RoomClassService getRoomClassService() {
        if (roomClassService == null) {
            synchronized (this) {
                if (roomClassService == null) {
                    roomClassService = ApplicationContext.getInstance().getBean(RoomClassService.class);
                }
            }
        }
        return roomClassService;
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
