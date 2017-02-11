package com.vaka.hotel_manager.service.impl;

import com.vaka.hotel_manager.core.context.ApplicationContextHolder;
import com.vaka.hotel_manager.core.security.SecurityService;
import com.vaka.hotel_manager.core.security.SecurityUtils;
import com.vaka.hotel_manager.domain.entity.User;
import com.vaka.hotel_manager.repository.UserRepository;
import com.vaka.hotel_manager.repository.exception.ConstraintViolationException;
import com.vaka.hotel_manager.repository.exception.ConstraintViolationType;
import com.vaka.hotel_manager.service.UserService;
import com.vaka.hotel_manager.util.exception.CreatingException;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;


/**
 * Created by Iaroslav on 12/1/2016.
 */
public class UserServiceImpl implements UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
    private UserRepository userRepository;
    private SecurityService securityService;

    @Override
    public User create(User loggedUser, User user) {
        LOG.debug("Creating user: {}", user);

        user.setPassword(generatePassword(user));
        try {
            return getUserRepository().create(user);
        } catch (ConstraintViolationException e) {
            if (e.getViolationType() == ConstraintViolationType.DUPLICATE_ENTRY && e.getViolatedField().equals("email"))
                throw new CreatingException("EmailExistException");
            else throw e;
        }
    }


    private String generatePassword(User user) {
        return BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
    }

    @Override
    public Optional<User> getById(User loggedUser, Integer id) {
        LOG.debug("Getting user by id: {}", id);
        Optional<User> requested = getUserRepository().getById(id);
        if (requested.isPresent()) {
            if (!id.equals(loggedUser.getId()))
                getSecurityService().authorize(loggedUser, SecurityUtils.MANAGER_ACCESS_ROLES);
        }
        eraseSensitivityCredentials(requested.get());
        return requested;
    }

    private User eraseSensitivityCredentials(User user) {
        user.setPassword("");
        return user;
    }

    @Override
    public boolean delete(User loggedUser, Integer id) {
        LOG.debug("Deleting user by id: {}", id);
        getSecurityService().authorize(loggedUser, SecurityUtils.MANAGER_ACCESS_ROLES);
        return getUserRepository().delete(id);
    }

    @Override
    public boolean update(User loggedUser, Integer id, User user) {
        LOG.debug("Updating user with id: %s, state: {}", id, user);
        if (!loggedUser.getId().equals(id))
            getSecurityService().authorize(loggedUser, SecurityUtils.MANAGER_ACCESS_ROLES);
        return getUserRepository().update(id, user);
    }

    public UserRepository getUserRepository() {
        if (userRepository == null) {
            userRepository = ApplicationContextHolder.getContext().getBean(UserRepository.class);
        }
        return userRepository;
    }

    public SecurityService getSecurityService() {
        if (securityService == null) {
            securityService = ApplicationContextHolder.getContext().getBean(SecurityService.class);
        }
        return securityService;
    }
}
