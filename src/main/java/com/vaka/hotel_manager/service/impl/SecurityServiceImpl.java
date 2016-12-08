package com.vaka.hotel_manager.service.impl;

import com.vaka.hotel_manager.context.ApplicationContext;
import com.vaka.hotel_manager.domain.Role;
import com.vaka.hotel_manager.domain.User;
import com.vaka.hotel_manager.repository.SecurityRepository;
import com.vaka.hotel_manager.repository.UserRepository;
import com.vaka.hotel_manager.service.SecurityService;
import com.vaka.hotel_manager.util.exception.AuthenticationException;
import com.vaka.hotel_manager.util.exception.AuthorizationException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by Iaroslav on 11/26/2016.
 */
public class SecurityServiceImpl implements SecurityService {
    private SecurityRepository securityRepository;
    private UserRepository userRepository;
    private User anonymous = new User();

    {
        anonymous.setRole(Role.ANONYMOUS);
    }

    private void createToken(HttpServletRequest req, HttpServletResponse resp, User loggedUser) throws AuthenticationException {
        String token = UUID.randomUUID().toString();
        getSecurityRepository().create(token, loggedUser.getId());
        Cookie cookie = new Cookie("TOKEN", token);
        cookie.setPath("/");
        resp.addCookie(cookie);
    }
//TODO change to BCRYPT
    private User checkCredentials(HttpServletRequest req, HttpServletResponse resp, String email, String password) throws AuthenticationException {
        Optional<User> user = getUserRepository().getByEmail(email);
        password = Base64.getEncoder().encodeToString(String.join(":", email, password).getBytes());

        if (!user.isPresent() || (!user.get().getPassword().equals(password))) {
            req.setAttribute("exception", "Login or/and password are incorrect");
            throw new AuthenticationException("Login or/and password are incorrect");
        }
        return user.get();
    }

    @Override
    public void signIn(HttpServletRequest req, HttpServletResponse resp, String email, String password) throws AuthenticationException {
        User user = checkCredentials(req, resp, email, password);
        createToken(req, resp, user);
    }
//TODO go to session
    //TODO move req and resp only to controllers
    @Override
    public User authenticate(HttpServletRequest req, HttpServletResponse resp) {
        Cookie[] cookies = req.getCookies();
        if (cookies == null)
            return anonymous;
        Optional<Cookie> token = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("TOKEN")).findFirst();
        if (!token.isPresent())
            return anonymous;
        Optional<Integer> userId = getSecurityRepository().getByToken(token.get().getValue());
        if (userId.isPresent()) {
            Optional<User> user = getUserRepository().getById(userId.get());
            if (user.isPresent())
                return eraseCredentials(user.get());
        }
        token.get().setMaxAge(0);
        return anonymous;
    }

    private User eraseCredentials(User user) {
        user.setPassword("");
        return user;
    }

    @Override
    public void logout(HttpServletRequest req, HttpServletResponse resp, User loggedUser) {
        Cookie[] cookies = req.getCookies();
        String userToken = "";
        for (Cookie cookie : cookies)
            if (cookie.getName().equals("TOKEN"))
                userToken = cookie.getValue();
        getSecurityRepository().delete(userToken);
        Cookie cookie = new Cookie("TOKEN", "deleted");
//        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        resp.addCookie(cookie);
    }

    public SecurityRepository getSecurityRepository() {
        if (securityRepository == null) {
            synchronized (this) {
                if (securityRepository == null) {
                    securityRepository = ApplicationContext.getInstance().getBean(SecurityRepository.class);
                }
            }
        }
        return securityRepository;
    }

    public UserRepository getUserRepository() {
        if (userRepository == null) {
            synchronized (this) {
                if (userRepository == null) {
                    userRepository = ApplicationContext.getInstance().getBean(UserRepository.class);
                }
            }
        }
        return userRepository;
    }
}