package com.vaka.hotel_manager.service;

import com.vaka.hotel_manager.EntityProviderUtil;
import com.vaka.hotel_manager.core.context.ApplicationContextHolder;
import com.vaka.hotel_manager.domain.entity.User;
import com.vaka.hotel_manager.repository.CrudRepository;
import com.vaka.hotel_manager.repository.UserRepository;

/**
 * Created by Iaroslav on 12/16/2016.
 */
public class UserServiceTest extends CrudServiceTest<User> {

    private UserRepository userRepositoryMock = ApplicationContextHolder.getContext().getBean(UserRepository.class);

    private UserService userService = ApplicationContextHolder.getContext().getBean(UserService.class);

    @Override
    protected CrudService<User> getService() {
        return userService;
    }

    @Override
    protected CrudRepository<User> getMockedRepository() {
        return userRepositoryMock;
    }

    @Override
    protected User createEntity() {
        return EntityProviderUtil.createUser();
    }
}
