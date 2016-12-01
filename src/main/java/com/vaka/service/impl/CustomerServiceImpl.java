package com.vaka.service.impl;

import com.vaka.domain.User;
import com.vaka.domain.User;
import com.vaka.repository.CustomerRepository;
import com.vaka.service.CustomerService;
import com.vaka.util.ApplicationContext;

/**
 * Created by Iaroslav on 12/1/2016.
 */
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository customerRepository;

    @Override
    public User create(User entity) {
        return getCustomerRepository().create(entity);
    }

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
