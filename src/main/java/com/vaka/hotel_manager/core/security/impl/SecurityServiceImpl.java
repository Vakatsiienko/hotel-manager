package com.vaka.hotel_manager.core.security.impl;

import com.vaka.hotel_manager.core.context.ApplicationContextHolder;
import com.vaka.hotel_manager.core.security.SecurityService;
import com.vaka.hotel_manager.core.tx.TransactionManager;
import com.vaka.hotel_manager.domain.Role;
import com.vaka.hotel_manager.domain.entity.User;
import com.vaka.hotel_manager.repository.UserRepository;
import com.vaka.hotel_manager.util.exception.AuthenticationException;
import com.vaka.hotel_manager.util.exception.AuthorizationException;
import com.vaka.hotel_manager.util.exception.CreatingException;
import com.vaka.hotel_manager.webservice.VkService;
import com.vaka.hotel_manager.webservice.jsonobject.VkAuthRequest;
import com.vaka.hotel_manager.webservice.jsonobject.VkUserInfo;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;

/**
 * Created by Iaroslav on 11/26/2016.
 */
public class SecurityServiceImpl implements SecurityService {
    private static final Logger LOG = LoggerFactory.getLogger(SecurityServiceImpl.class);
    private static final User anonymous = new User();
    private UserRepository userRepository;
    private TransactionManager transactionManager;
    private VkService vkService;


    static {
        anonymous.setRole(Role.ANONYMOUS);
    }

    @Override
    public boolean signInVk(HttpSession session, String code) {
        VkAuthRequest authRequest = getVkService().signIn(code);
        Optional<User> existed = getTransactionManager().doTransactional(
                () -> {
                    Optional<User> byVkId = getUserRepository().getByVkId(authRequest.getUserId());
                    if (!byVkId.isPresent()) {
                        Optional<User> byEmail = getUserRepository().getByEmail(authRequest.getEmail());
                        if (byEmail.isPresent()) {
                            session.setAttribute("email", byEmail.get().getEmail());
                            //if email exist - fail-fast
                            throw new CreatingException("EmailExistException");
                        }
                    }
                    return byVkId;
                });
        if (existed.isPresent()){
            eraseSensivityCredentials(existed.get());
            session.setAttribute("loggedUser", existed.get());
            return true;
        } else {
            VkUserInfo userInfo = getVkService().getSignUpInfo(authRequest.getAccessToken());
            User user = new User();
            user.setRole(Role.CUSTOMER);
            user.setVkId(authRequest.getUserId());
            user.setName(new StringJoiner(" ")
                    .add(userInfo.getFirstName())
                    .add(userInfo.getLastName()).toString());
            user.setEmail(authRequest.getEmail());
            session.setAttribute("vkUser", user);
            return false;
        }
    }

    private void eraseSensivityCredentials(User user) {
        user.setPassword("");
    }

    @Override
    public void authorize(User loggedUser, Set<Role> expectedRoles) {
        LOG.debug("LoggedUser Authorization, actual role: {}, expected roles: {}", loggedUser.getRole(), expectedRoles);
        if (!expectedRoles.contains(loggedUser.getRole())) {
            throw new AuthorizationException("Not Allowed");
        }
    }

    @Override
    public void signIn(HttpSession session, String email, String password) {
        LOG.debug("Signin user, email: {}", email);
        User user = getUserByCredentials(email, password);
        session.setAttribute("loggedUser", user);
    }

    private User getUserByCredentials(String email, String password) {
        LOG.debug("Searching and checking user password with email: {}", email);
        Optional<User> user = getTransactionManager().doTransactional(() -> getUserRepository().getByEmail(email));

        if (!user.isPresent() ||
                (!BCrypt.checkpw(password, user.get().getPassword()))) {
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
            userRepository = ApplicationContextHolder.getContext().getBean(UserRepository.class);
        }
        return userRepository;
    }

    public TransactionManager getTransactionManager() {
        if (transactionManager == null) {
            transactionManager = ApplicationContextHolder.getContext().getBean(TransactionManager.class);
        }
        return transactionManager;
    }
    public VkService getVkService() {
        if (vkService == null) {
            vkService = ApplicationContextHolder.getContext().getBean(VkService.class);
        }
        return vkService;
    }
}
