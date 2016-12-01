package com.vaka.service.impl;

import com.vaka.repository.ManagerRepository;
import com.vaka.service.ManagerService;
import com.vaka.util.ApplicationContext;

/**
 * Created by Iaroslav on 11/26/2016.
 */
public class ManagerServiceImpl implements ManagerService {
    private ManagerRepository userRepository;

    @Override
    public com.vaka.domain.Manager create(com.vaka.domain.Manager entity) {
        return getUserRepository().create(entity);
    }

    @Override
    public com.vaka.domain.Manager getById(Integer id) {
        return getUserRepository().getById(id);
    }

    @Override
    public boolean delete(Integer id) {
        return getUserRepository().delete(id);
    }

    @Override
    public com.vaka.domain.Manager update(Integer id, com.vaka.domain.Manager entity) {
        return getUserRepository().update(id, entity);
    }


    public ManagerRepository getUserRepository() {
        if (userRepository == null) {
            synchronized (this) {
                if (userRepository == null) {
                    userRepository = ApplicationContext.getBean(ManagerRepository.class);
                }
            }
        }
        return userRepository;
    }
}
