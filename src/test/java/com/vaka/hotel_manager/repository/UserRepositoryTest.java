package com.vaka.hotel_manager.repository;

import com.vaka.hotel_manager.DBTestUtil;
import com.vaka.hotel_manager.EntityProviderUtil;
import com.vaka.hotel_manager.domain.entities.User;
import com.vaka.hotel_manager.core.context.ApplicationContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Created by Iaroslav on 12/3/2016.
 */
public class UserRepositoryTest extends CrudRepositoryTest<User> {
    private UserRepository userRepository = ApplicationContext.getInstance().getBean(UserRepository.class);

    @Before
    public void setUp() throws SQLException, ClassNotFoundException, IOException {
        DBTestUtil.reset();
    }

    @Test
    public void testGetByEmail() throws Exception {
        userRepository.create(createEntity());
        User user = createEntity();
        String mail = "someNewRandomEmail@mail";
        user.setEmail(mail);
        User expected =  userRepository.create(user);
        userRepository.create(createEntity());

        Optional<User> actual = userRepository.getByEmail(mail);

        Assert.assertEquals(expected, actual.get());
        Assert.assertEquals(mail, actual.get().getEmail());
    }

    @Override
    protected User getEntityForUpdate(User entity) {
        User user = createEntity();
        user.setVkId(entity.getVkId());
        return user;
    }

    @Override
    protected CrudRepository<User> getRepository() {
        return userRepository;
    }

    @Override
    protected User createEntity() {
        return EntityProviderUtil.createUser();
    }
}
