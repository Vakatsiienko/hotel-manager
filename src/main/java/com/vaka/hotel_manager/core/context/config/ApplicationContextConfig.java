package com.vaka.hotel_manager.core.context.config;


import com.vaka.hotel_manager.core.security.SecurityService;
import com.vaka.hotel_manager.core.security.impl.SecurityServiceImpl;
import com.vaka.hotel_manager.core.tx.TransactionHelper;
import com.vaka.hotel_manager.repository.*;
import com.vaka.hotel_manager.repository.jdbcImpl.*;
import com.vaka.hotel_manager.core.tx.ConnectionManager;
import com.vaka.hotel_manager.core.tx.JdbcTransactionManagerImpl;
import com.vaka.hotel_manager.core.tx.TransactionManager;
import com.vaka.hotel_manager.repository.util.JdbcCrudHelper;
import com.vaka.hotel_manager.service.*;
import com.vaka.hotel_manager.service.impl.*;
import com.vaka.hotel_manager.web.controller.*;
import com.vaka.hotel_manager.webservice.VkService;
import com.vaka.hotel_manager.webservice.impl.VkServiceImpl;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Iaroslav on 11/26/2016.
 */
public class ApplicationContextConfig {

    @Getter
    private Map<Class<?>, Class<?>> implClassByBeanName;

    @Getter
    private Map<Class<?>, Object> implBeanByBeanName;

    public ApplicationContextConfig() {
        implBeanByBeanName = new HashMap<>();
        implClassByBeanName = new HashMap<>();

        //Controllers
        implClassByBeanName.put(RoomController.class, RoomController.class);
        implClassByBeanName.put(BillController.class, BillController.class);
        implClassByBeanName.put(UserController.class, UserController.class);
        implClassByBeanName.put(ReservationController.class, ReservationController.class);
        implClassByBeanName.put(RoomClassController.class, RoomClassController.class);

        //Services
        implClassByBeanName.put(RoomService.class, RoomServiceImpl.class);
        implClassByBeanName.put(BillService.class, BillServiceImpl.class);
        implClassByBeanName.put(UserService.class, UserServiceImpl.class);
        implClassByBeanName.put(SecurityService.class, SecurityServiceImpl.class);
        implClassByBeanName.put(ReservationService.class, ReservationServiceImpl.class);
        implClassByBeanName.put(RoomClassService.class, RoomClassServiceImpl.class);
        implClassByBeanName.put(VkService.class, VkServiceImpl.class);

        //Repository
        implClassByBeanName.put(RoomRepository.class, RoomRepositoryJdbcImpl.class);
        implClassByBeanName.put(BillRepository.class, BillRepositoryJdbcImpl.class);
        implClassByBeanName.put(UserRepository.class, UserRepositoryJdbcImpl.class);
        implClassByBeanName.put(ReservationRepository.class, ReservationRepositoryJdbcImpl.class);
        implClassByBeanName.put(RoomClassRepository.class, RoomClassRepositoryJdbcImpl.class);

        //Other

        TransactionManager transactionManager = new JdbcTransactionManagerImpl(TransactionManager.TRANSACTION_READ_COMMITTED);
        implBeanByBeanName.put(TransactionManager.class, transactionManager);

        TransactionHelper transactionHelper = new TransactionHelper(transactionManager);
        implBeanByBeanName.put(TransactionHelper.class, transactionHelper);

        ConnectionManager connectionManager = (ConnectionManager) transactionManager;
        implBeanByBeanName.put(ConnectionManager.class, connectionManager);

        JdbcCrudHelper crudHelper = new JdbcCrudHelper(connectionManager);
        implBeanByBeanName.put(JdbcCrudHelper.class, crudHelper);
    }
}
