package com.vaka.service.impl;

import com.vaka.domain.Role;
import com.vaka.domain.User;
import com.vaka.repository.CustomerRepository;
import com.vaka.repository.SecurityRepository;
import com.vaka.service.SecurityService;
import com.vaka.util.ApplicationContext;
import com.vaka.util.exception.AuthenticateException;
import com.vaka.util.exception.AuthorizationException;

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
    private CustomerRepository customerRepository;
    private User anonymous = new User();

    {
        anonymous.setRole(Role.ANONYMOUS);
    }

    @Override
    public void createToken(HttpServletRequest req, HttpServletResponse resp, User loggedUser) throws AuthenticateException {
        String token = UUID.randomUUID().toString();
        getSecurityRepository().create(token, loggedUser.getId());
        Cookie cookie = new Cookie("TOKEN", token);
        cookie.setPath("/");
        resp.addCookie(cookie);
    }

    @Override
    public User checkCredentials(HttpServletRequest req, HttpServletResponse resp, String email, String password) throws AuthenticateException{
        User user = getCustomerRepository().getByEmail(email);
        password = Base64.getEncoder().encodeToString(String.join(":", email, password).getBytes());

        if ((user == null) || (!user.getPassword().equals(password))) {
            req.setAttribute("exception", "Login or/and password are incorrect");
            throw new AuthenticateException("Login or/and password are incorrect");
        }
        return user;
    }

    @Override
    public User authenticate(HttpServletRequest req, HttpServletResponse resp) {
        Cookie[] cookies = req.getCookies();
        if (cookies == null)
            return anonymous;
            Optional<Cookie> token = Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals("TOKEN")).findFirst();
        if (!token.isPresent())
            return anonymous;
        Integer userId = getSecurityRepository().getByToken(token.get().getValue());
        User user = getCustomerRepository().getById(userId);
        if (user != null)
            return user;
        token.get().setMaxAge(0);
        return anonymous;
    }

    @Override
    public void authorize(User loggedUser, Role expected) {
        if (expected != loggedUser.getRole() && loggedUser.getRole() != Role.MANAGER) {
            throw new AuthorizationException(String.format("Expected role: %s, actual: %s", expected, loggedUser.getRole()));
        }
    }

    @Override
    public void logout(HttpServletRequest req, HttpServletResponse resp, String token) {
        getSecurityRepository().delete(token);
    }

    public SecurityRepository getSecurityRepository() {
        if (securityRepository == null) {
            synchronized (this) {
                if (securityRepository == null) {
                    securityRepository = ApplicationContext.getBean(SecurityRepository.class);
                }
            }
        }
        return securityRepository;
    }

    public CustomerRepository getCustomerRepository() {
        if (customerRepository == null) {
            synchronized (this) {
                if (customerRepository == null) {
                    customerRepository = ApplicationContext.getBean(CustomerRepository.class);
                }
            }
        }
        return customerRepository;
    }
}
