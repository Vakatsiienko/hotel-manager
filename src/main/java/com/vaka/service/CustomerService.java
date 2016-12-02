package com.vaka.service;

import com.vaka.domain.User;
import com.vaka.domain.User;

/**
 * Created by Iaroslav on 12/1/2016.
 */
public interface CustomerService extends CrudService<User> {

    User createOrUpdate(User user);

}
