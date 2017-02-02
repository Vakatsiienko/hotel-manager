package com.vaka.hotel_manager.web.servlet;

import com.vaka.hotel_manager.core.context.ApplicationContext;
import com.vaka.hotel_manager.domain.Role;
import com.vaka.hotel_manager.domain.entities.User;
import com.vaka.hotel_manager.core.security.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Iaroslav on 12/16/2016.
 */
public class SecurityFilter implements Filter {
    private SecurityService securityService;
    private static final Logger LOG = LoggerFactory.getLogger(SecurityFilter.class);
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
            if (loggedUser.getRole() == Role.ANONYMOUS) {
                if ("/signin".equals(uri) || "/signup".equals(uri) ||
                        "/".equals(uri) || "/rooms".equals(uri) || "/reservations".equals(uri)|| "/signup-vk".equals(uri));
                else {
                    LOG.debug("Anonymous trying to get {}, but was redirected to /signin", uri);
                    ((HttpServletResponse) response).sendRedirect(String.format("/signin?redirectUri=%s", uri));
                    return;
                }
            } else if (loggedUser.getRole() != Role.ANONYMOUS){
                if ("/signin".equals(uri) || "/signup".equals(uri) || "/signup-vk".equals(uri)){
                    LOG.debug("Logged user trying to get {}, but was redirected to his user page");
                    ((HttpServletResponse) response).sendRedirect("/users/" + loggedUser.getId());
                    return;
                }
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
            synchronized (this){
                securityService = ApplicationContext.getInstance().getBean(SecurityService.class);
            }
        }
        return securityService;
    }
}
