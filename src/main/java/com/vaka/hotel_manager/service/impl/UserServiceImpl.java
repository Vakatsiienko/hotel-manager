package com.vaka.hotel_manager.service.impl;

import com.vaka.hotel_manager.context.ApplicationContext;
import com.vaka.hotel_manager.domain.User;
import com.vaka.hotel_manager.repository.UserRepository;
import com.vaka.hotel_manager.service.SecurityService;
import com.vaka.hotel_manager.service.UserService;
import com.vaka.hotel_manager.util.SecurityUtil;
import com.vaka.hotel_manager.util.exception.CreatingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

/**
 * Created by Iaroslav on 12/1/2016.
 */
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private SecurityService securityService;
    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public User create(User loggedUser, User user) {
        synchronized (this) {
            LOG.debug("Creating user: {}", user);
            if (getUserRepository().getByEmail(user.getEmail()).isPresent()) {
                throw new CreatingException("User with such email already exist.");
            }
            user.setPassword(SecurityUtil.generatePassword(user));
            user.setCreatedDatetime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
            return getUserRepository().create(user);
        }
    }

    @Override
    public Optional<User> getById(User loggedUser, Integer id) {
        LOG.debug("Getting user by id: {}", id);
        Optional<User> requested = getUserRepository().getById(id);
        if (requested.isPresent()) {
            SecurityUtil.eraseSensitivityCredentials(requested.get());
            if (!id.equals(loggedUser.getId()))
                getSecurityService().authorize(loggedUser, SecurityUtil.MANAGER_ACCESS_ROLES);
        }
        return requested;
    }

    @Override
    public boolean delete(User loggedUser, Integer id) {
        LOG.debug("Deleting user by id: {}", id);
        getSecurityService().authorize(loggedUser, SecurityUtil.MANAGER_ACCESS_ROLES);
        return getUserRepository().delete(id);
    }

    @Override
    public boolean update(User loggedUser, Integer id, User user) {
        LOG.debug("Updating user with id: %s, state: {}", id, user);
        if (!loggedUser.getId().equals(id))
            getSecurityService().authorize(loggedUser, SecurityUtil.MANAGER_ACCESS_ROLES);
        return getUserRepository().update(id, user);
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

    public SecurityService getSecurityService() {
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
