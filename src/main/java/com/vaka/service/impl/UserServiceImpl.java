package com.vaka.service.impl;

import com.vaka.domain.Manager;
import com.vaka.repository.UserRepository;
import com.vaka.service.UserService;
import com.vaka.util.ApplicationContext;

/**
 * Created by Iaroslav on 11/26/2016.
 */
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Override
    public Manager create(Manager entity) {
        return getUserRepository().persist(entity);
    }

    @Override
    public Manager getById(Integer id) {
        return getUserRepository().getById(id);
    }

    @Override
    public boolean delete(Integer id) {
        return getUserRepository().delete(id);
    }

    @Override
    public Manager update(Integer id, Manager entity) {
        return getUserRepository().update(id, entity);
    }


    public UserRepository getUserRepository() {
        if (userRepository == null) {
            synchronized (this) {
                if (userRepository == null) {
                    userRepository = ApplicationContext.getBean(UserRepository.class);
                }
            }
        }
        return userRepository;
    }
}
