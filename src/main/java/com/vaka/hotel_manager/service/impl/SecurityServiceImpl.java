package com.vaka.hotel_manager.service.impl;

import com.vaka.hotel_manager.core.tx.TransactionHelper;
import com.vaka.hotel_manager.core.tx.TransactionManager;
import com.vaka.hotel_manager.core.context.ApplicationContext;
import com.vaka.hotel_manager.domain.Role;
import com.vaka.hotel_manager.domain.User;
import com.vaka.hotel_manager.repository.UserRepository;
import com.vaka.hotel_manager.service.SecurityService;
import com.vaka.hotel_manager.util.SecurityUtil;
import com.vaka.hotel_manager.util.exception.AuthenticationException;
import com.vaka.hotel_manager.util.exception.AuthorizationException;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Iaroslav on 11/26/2016.
 */
public class SecurityServiceImpl implements SecurityService {
    private static final Logger LOG = LoggerFactory.getLogger(SecurityServiceImpl.class);
    private static final User anonymous = new User();
    private UserRepository userRepository;
    private TransactionHelper transactionHelper;


    static {
        anonymous.setRole(Role.ANONYMOUS);
    }

    @Override
    public void authorize(User loggedUser, Set<Role> expectedRoles) {
        LOG.debug("LoggedUser Authorization, actual role: {}, expected roles: {}", loggedUser.getRole(), expectedRoles);
        if (!expectedRoles.contains(loggedUser.getRole())) {
            throw new AuthorizationException("Not Allowed");
        }
    }

    @Override
    public void signIn(HttpSession session, String email, String password) throws AuthenticationException {
        LOG.debug("Signin user, email: {}", email);
        User user = getUserByCredentials(email, password);
        session.setAttribute("loggedUser", SecurityUtil.eraseSensitivityCredentials(user));
    }

    private User getUserByCredentials(String email, String password) throws AuthenticationException {
        LOG.debug("Searching and checking user password with email: {}", email);
        Optional<User> user = getTransactionHelper().doTransactional(() -> getUserRepository().getByEmail(email));

        if (!user.isPresent() || (!BCrypt.checkpw(password, user.get().getPassword()))) {
            throw new AuthenticationException("SignInException");
        }
        return user.get();
    }

    @Override
    public User authenticate(HttpSession session) {
        LOG.debug("Authenticating user");
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser != null) {
            LOG.debug("Authenticated: {}", loggedUser);
            return loggedUser;
        }
        session.setAttribute("loggedUser", anonymous);
        LOG.debug("Authenticated: {}", anonymous);
        return anonymous;
    }



    @Override
    public void logout(HttpSession session) {
        session.invalidate();
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
    public TransactionHelper getTransactionHelper() {
        if (transactionHelper == null) {
            synchronized (this) {
                if (transactionHelper == null) {
                    transactionHelper = ApplicationContext.getInstance().getBean(TransactionHelper.class);
                }
            }
        }
        return transactionHelper;
    }
}
