package com.vaka.service.impl;

import com.vaka.domain.User;
import com.vaka.domain.User;
import com.vaka.repository.CustomerRepository;
import com.vaka.service.CustomerService;
import com.vaka.util.ApplicationContext;
import com.vaka.util.exception.CreatingException;

import java.util.Base64;

/**
 * Created by Iaroslav on 12/1/2016.
 */
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository customerRepository;

    @Override
    public User create(User entity) {
        if (getCustomerRepository().getByEmail(entity.getEmail()) != null) {
            throw new CreatingException("User with such email already exist.");
        }
        entity.setPassword(Base64.getEncoder().encodeToString(String.join(":", entity.getEmail(), entity.getPassword()).getBytes()));
        return getCustomerRepository().create(entity);
    }

    @Override
    public User createOrUpdate(User user) {
        User registered = getCustomerRepository().getByEmail(user.getEmail());
        if (registered != null) {
            registered.setPhoneNumber(user.getPhoneNumber());
            registered.setName(user.getName());
            //update password if user contains it
            if (user.getPassword() != null)
                registered.setPassword(Base64.getEncoder().encodeToString(String.join(":", registered.getEmail(), registered.getPassword()).getBytes()));
            return getCustomerRepository().update(registered.getId(), registered);
        } else {
            user.setPassword(Base64.getEncoder().encodeToString(String.join(":", user.getEmail(), user.getPassword()).getBytes()));
            return getCustomerRepository().create(user);
        }
    }//TODO split changing password to separate methods


    @Override
    public User getById(Integer id) {
        return getCustomerRepository().getById(id);
    }

    @Override
    public boolean delete(Integer id) {
        return getCustomerRepository().delete(id);
    }

    @Override
    public User update(Integer id, User entity) {
        return getCustomerRepository().update(id, entity);
    }

    public CustomerRepository getCustomerRepository() {
        if (customerRepository == null){
            synchronized (this) {
                if (customerRepository == null) {
                    customerRepository = ApplicationContext.getBean(CustomerRepository.class);
                }
            }
        }
        return customerRepository;
    }
}
