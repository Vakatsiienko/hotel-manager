package com.vaka.service.impl;

import com.vaka.context.ApplicationContext;
import com.vaka.domain.Role;
import com.vaka.domain.User;
import com.vaka.repository.UserRepository;
import com.vaka.service.UserService;
import com.vaka.util.SecurityUtil;
import com.vaka.util.exception.AuthorizationException;
import com.vaka.util.exception.CreatingException;
import com.vaka.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Optional;

/**
 * Created by Iaroslav on 12/1/2016.
 */
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Override
    public User create(User loggedUser, User entity) {
        if (getUserRepository().getByEmail(entity.getEmail()).isPresent()) {
            throw new CreatingException("User with such email already exist.");
        }
        entity.setPassword(SecurityUtil.generatePassword(entity));
        entity.setCreatedDatetime(LocalDateTime.now(ZoneId.of("UTC")).truncatedTo(ChronoUnit.SECONDS));
        return getUserRepository().create(entity);
    }

    @Override
    public Optional<User> getById(User loggedUser, Integer id) {
        Optional<User> requested = getUserRepository().getById(id);
        if (requested.isPresent())
            if (loggedUser.getId().equals(requested.get().getId()))
                return requested;
        if (loggedUser.getRole() == Role.MANAGER)
            return getUserRepository().getById(id);
        else throw new AuthorizationException("Not Allowed.");
    }

    @Override
    public boolean delete(User loggedUser, Integer id) {
        return getUserRepository().delete(id);
    }

    @Override
    public boolean update(User loggedUser, Integer id, User entity) {
        return getUserRepository().update(id, entity);
    }

    @Override
    public boolean updateWithoutPassword(User loggedUser, Integer id, User entity) {
        Optional<User> user = getById(loggedUser, id);
        if (user.isPresent()) {
            entity.setPassword(user.get().getPassword());
            return getUserRepository().update(id, entity);
        } else throw new NotFoundException("No user with given id");
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
