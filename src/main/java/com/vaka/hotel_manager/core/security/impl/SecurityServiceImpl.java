package com.vaka.hotel_manager.core.security.impl;

import com.vaka.hotel_manager.core.tx.TransactionHelper;
import com.vaka.hotel_manager.core.context.ApplicationContext;
import com.vaka.hotel_manager.domain.Role;
import com.vaka.hotel_manager.domain.User;
import com.vaka.hotel_manager.repository.UserRepository;
import com.vaka.hotel_manager.core.security.SecurityService;
import com.vaka.hotel_manager.service.VkService;
import com.vaka.hotel_manager.util.exception.AuthenticationException;
import com.vaka.hotel_manager.util.exception.AuthorizationException;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.util.Map;
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
    private TransactionHelper transactionHelper;
    private VkService vkService;


    static {
        anonymous.setRole(Role.ANONYMOUS);
    }

    @Override
    public boolean signInVk(HttpSession session, String code) {
        User loggedUser = authenticate(session);
        if (loggedUser.getRole() != Role.ANONYMOUS)
            throw new AuthenticationException();//TODO move anonymous authorization to filter
        Map<String, String> parsedJsonTokenRequest = getVkService().signIn(code);
        String vkId = parsedJsonTokenRequest.get("user_id");
        String accessToken = parsedJsonTokenRequest.get("access_token");
        String email = parsedJsonTokenRequest.get("email");
        if (vkId == null || accessToken == null || email == null)
            throw new AuthorizationException("Vk authorization failed");
        Optional<User> existed = getTransactionHelper().doTransactional(
                () -> getUserRepository().getByVkId(vkId));
//        session.setAttribute("token_request", parsedJsonTokenRequest);
        if (existed.isPresent()){
            session.setAttribute("loggedUser", existed.get());
            return true;
        } else {
            //TODO add case when user with such email exist
            Map<String, String> userInfo = getVkService().getSignUpInfo(accessToken);
            User user = new User();
            user.setRole(Role.CUSTOMER);
            user.setVkId(vkId);
            user.setName(new StringJoiner(" ")
                    .add(userInfo.getOrDefault("first_name",""))
                    .add(userInfo.getOrDefault("last_name", "")).toString());
            user.setEmail(email);
            session.setAttribute("vkUser", user);
            return false;
        }
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
        session.setAttribute("loggedUser", user);
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
    public VkService getVkService() {
        if (vkService == null) {
            synchronized (this) {
                if (vkService == null) {
                    vkService = ApplicationContext.getInstance().getBean(VkService.class);
                }
            }
        }
        return vkService;
    }
}
