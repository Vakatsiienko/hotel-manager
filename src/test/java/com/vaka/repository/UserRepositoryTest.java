package com.vaka.repository;

import com.vaka.EntityProviderUtil;
import com.vaka.domain.User;
import com.vaka.util.ApplicationContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Iaroslav on 12/3/2016.
 */
public class UserRepositoryTest extends CrudRepositoryTest<User> {
    private CustomerRepository customerRepository = ApplicationContext.getBean(CustomerRepository.class);
    @Before
    public void setUp(){
        ApplicationContext.init();
    }
    @Test
    public void testGetByEmail() throws Exception {
        customerRepository.create(createEntity());
        User user = createEntity();
        String mail = "someNewEmail@mail";
        user.setEmail(mail);
        User expected =  customerRepository.create(user);
        customerRepository.create(createEntity());

        User actual = customerRepository.getByEmail(mail);

        Assert.assertEquals(expected, actual);
        Assert.assertEquals(mail, actual.getEmail());
    }

    @Override
    protected CrudRepository<User> getRepository() {
        return customerRepository;
    }

    @Override
    protected User createEntity() {
        return EntityProviderUtil.createUser();
    }
}
