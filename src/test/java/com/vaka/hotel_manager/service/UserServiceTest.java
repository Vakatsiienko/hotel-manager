package com.vaka.hotel_manager.service;

import com.vaka.hotel_manager.EntityProviderUtil;
import com.vaka.hotel_manager.core.context.ApplicationContext;
import com.vaka.hotel_manager.domain.User;
import com.vaka.hotel_manager.repository.CrudRepository;
import com.vaka.hotel_manager.repository.UserRepository;

/**
 * Created by Iaroslav on 12/16/2016.
 */
public class UserServiceTest extends CrudServiceTest<User> {

    private UserRepository userRepositoryMock = ApplicationContext.getInstance().getBean(UserRepository.class);

    private UserService userService = ApplicationContext.getInstance().getBean(UserService.class);

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
