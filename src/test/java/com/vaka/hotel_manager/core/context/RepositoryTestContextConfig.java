package com.vaka.hotel_manager.core.context;

import com.vaka.hotel_manager.core.TestConnectionManagerImpl;
import com.vaka.hotel_manager.core.context.config.ApplicationContextConfig;
import com.vaka.hotel_manager.core.tx.ConnectionManager;
import com.vaka.hotel_manager.repository.*;
import com.vaka.hotel_manager.repository.jdbcImpl.*;
import com.vaka.hotel_manager.repository.util.JdbcCrudHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Iaroslav on 1/30/2017.
 */
public class RepositoryTestContextConfig extends ApplicationContextConfig {

    private Map<Class<?>, Class<?>> implClassByBeanName;

    private Map<Class<?>, Object> implBeanByBeanName;

    {
        implClassByBeanName = new HashMap<>();
        implBeanByBeanName = new HashMap<>();

        //Repository
        implClassByBeanName.put(RoomRepository.class, RoomRepositoryJdbcImpl.class);
        implClassByBeanName.put(BillRepository.class, BillRepositoryJdbcImpl.class);
        implClassByBeanName.put(UserRepository.class, UserRepositoryJdbcImpl.class);
        implClassByBeanName.put(ReservationRepository.class, ReservationRepositoryJdbcImpl.class);
        implClassByBeanName.put(RoomClassRepository.class, RoomClassRepositoryJdbcImpl.class);

        //Other
        ConnectionManager connectionManager = new TestConnectionManagerImpl();
        implBeanByBeanName.put(ConnectionManager.class, connectionManager);

        JdbcCrudHelper crudHelper = new JdbcCrudHelper(connectionManager);
        implBeanByBeanName.put(JdbcCrudHelper.class, crudHelper);

    }

    @Override
    public Map<Class<?>, Object> getImplBeanByBeanName() {
        return implBeanByBeanName;
    }
}
