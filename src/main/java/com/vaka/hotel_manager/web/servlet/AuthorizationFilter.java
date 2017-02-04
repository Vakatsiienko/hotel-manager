package com.vaka.hotel_manager.web.servlet;

import com.vaka.hotel_manager.core.context.ApplicationContextHolder;
import com.vaka.hotel_manager.core.security.SecurityService;
import com.vaka.hotel_manager.domain.Role;
import com.vaka.hotel_manager.domain.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Iaroslav on 12/16/2016.
 */
public class AuthorizationFilter implements Filter {
    public static final Set<String> ANONYMOUS_ONLY_URI;
    public static final Set<String> ANONYMOUS_GRANTED_URI;
    private static final Logger LOG = LoggerFactory.getLogger(AuthorizationFilter.class);


    static {
        Set<String> anonymousGrantedUri = new HashSet<>();
        anonymousGrantedUri.add("/signin");
        anonymousGrantedUri.add("/signup");
        anonymousGrantedUri.add("/signup-vk");
        ANONYMOUS_ONLY_URI = Collections.unmodifiableSet(anonymousGrantedUri);
        anonymousGrantedUri.add("/rooms");
        anonymousGrantedUri.add("/");
        ANONYMOUS_GRANTED_URI = Collections.unmodifiableSet(anonymousGrantedUri);
    }

    private SecurityService securityService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
//        No op
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest req = (HttpServletRequest) request;
            User loggedUser = getSecurityService().authenticate(req.getSession());
            String uri = req.getRequestURI();
            if (loggedUser.getRole() == Role.ANONYMOUS && !ANONYMOUS_GRANTED_URI.contains(uri)) {
                LOG.debug("Anonymous trying to get {}, but was redirected to /signin", uri);
                ((HttpServletResponse) response).sendRedirect(String.format("/signin?redirectUri=%s", uri));
                return;
            } else if (loggedUser.getRole() != Role.ANONYMOUS && ANONYMOUS_ONLY_URI.contains(uri)) {
                LOG.debug("Logged user trying to get {}, but was redirected to his user page", loggedUser.getId());
                ((HttpServletResponse) response).sendRedirect("/users/" + loggedUser.getId());
                return;
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
//    No op
    }

    public SecurityService getSecurityService() {
        if (securityService == null) {
            securityService = ApplicationContextHolder.getContext().getBean(SecurityService.class);
        }
        return securityService;
    }
}
