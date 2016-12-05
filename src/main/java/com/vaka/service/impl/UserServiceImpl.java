package com.vaka.service.impl;

import com.vaka.context.ApplicationContext;
import com.vaka.domain.Role;
import com.vaka.domain.User;
import com.vaka.repository.UserRepository;
import com.vaka.service.UserService;
import com.vaka.util.exception.AuthorizationException;
import com.vaka.util.exception.CreatingException;

import java.time.LocalDateTime;
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
        entity.setPassword(Base64.getEncoder().encodeToString(String.join(":", entity.getEmail(), entity.getPassword()).getBytes()));
        entity.setCreatedDatetime(LocalDateTime.now());
        return getUserRepository().create(entity);
    }

    @Override
    public Optional<User> getById(User loggedUser, Integer id) {
        if (loggedUser.getRole() == Role.MANAGER)
            return getUserRepository().getById(id);
        else throw new AuthorizationException("Not Allowed.");
    }


    @Override
    public User createOrUpdate(User loggedUser, User user) {
        Optional<User> registered = getUserRepository().getByEmail(user.getEmail());
        if (registered.isPresent()) {
            registered.get().setPhoneNumber(user.getPhoneNumber());
            registered.get().setName(user.getName());
            //update password if user contains it
            if (user.getPassword() != null)
                registered.get().setPassword(Base64.getEncoder().encodeToString(String.join(":", registered.get().getEmail(), registered.get().getPassword()).getBytes()));
            getUserRepository().update(registered.get().getId(), registered.get());
            return registered.get();
        } else {
            return create(loggedUser, user);
        }
    }//TODO move changing password to separate methods

    @Override
    public boolean delete(User loggedUser, Integer id) {
        return getUserRepository().delete(id);
    }

    @Override
    public boolean update(User loggedUser, Integer id, User entity) {
        return getUserRepository().update(id, entity);
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
