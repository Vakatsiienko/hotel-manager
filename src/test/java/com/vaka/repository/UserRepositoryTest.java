package com.vaka.repository;

import com.vaka.DBTestUtil;
import com.vaka.EntityProviderUtil;
import com.vaka.context.config.ApplicationContextConfig;
import com.vaka.context.config.PersistenceConfig;
import com.vaka.domain.User;
import com.vaka.context.ApplicationContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Optional;

/**
 * Created by Iaroslav on 12/3/2016.
 */
public class UserRepositoryTest extends CrudRepositoryTest<User> {
    private UserRepository userRepository = ApplicationContext.getInstance().getBean(UserRepository.class);
    @Before
    public void setUp() throws SQLException, ClassNotFoundException {
        ApplicationContext.getInstance().init(new ApplicationContextConfig(), new PersistenceConfig());
        DBTestUtil.reset();
    }
    @Test
    public void testGetByEmail() throws Exception {
        userRepository.create(createEntity());
        User user = createEntity();
        String mail = "someNewEmail@mail";
        user.setEmail(mail);
        User expected =  userRepository.create(user);
        userRepository.create(createEntity());

        Optional<User> actual = userRepository.getByEmail(mail);

        Assert.assertEquals(expected, actual.get());
        Assert.assertEquals(mail, actual.get().getEmail());
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
