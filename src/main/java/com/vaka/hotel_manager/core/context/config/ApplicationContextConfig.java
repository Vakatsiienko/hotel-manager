package com.vaka.hotel_manager.core.context.config;


import com.vaka.hotel_manager.core.security.SecurityService;
import com.vaka.hotel_manager.core.security.impl.SecurityServiceImpl;
import com.vaka.hotel_manager.repository.*;
import com.vaka.hotel_manager.repository.jdbcImpl.*;
import com.vaka.hotel_manager.service.*;
import com.vaka.hotel_manager.service.impl.*;
import com.vaka.hotel_manager.web.controller.*;
import com.vaka.hotel_manager.webservice.VkService;
import com.vaka.hotel_manager.webservice.impl.VkServiceImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Iaroslav on 11/26/2016.
 */
public class ApplicationContextConfig implements BeanConfig {


    private Map<Object, Class<?>> beanImplClassByBeanName;

    private Map<Object, Object> beanByBeanName;

    public ApplicationContextConfig() {
        beanByBeanName = new HashMap<>();
        beanImplClassByBeanName = new HashMap<>();

        //Controllers
        beanImplClassByBeanName.put(RoomController.class, RoomController.class);
        beanImplClassByBeanName.put(BillController.class, BillController.class);
        beanImplClassByBeanName.put(UserController.class, UserController.class);
        beanImplClassByBeanName.put(ReservationController.class, ReservationController.class);
        beanImplClassByBeanName.put(RoomClassController.class, RoomClassController.class);

        //Services
        beanImplClassByBeanName.put(RoomService.class, RoomServiceImpl.class);
        beanImplClassByBeanName.put(BillService.class, BillServiceImpl.class);
        beanImplClassByBeanName.put(UserService.class, UserServiceImpl.class);
        beanImplClassByBeanName.put(SecurityService.class, SecurityServiceImpl.class);
        beanImplClassByBeanName.put(ReservationService.class, ReservationServiceImpl.class);
        beanImplClassByBeanName.put(RoomClassService.class, RoomClassServiceImpl.class);
        beanImplClassByBeanName.put(VkService.class, VkServiceImpl.class);

        //Repository
        beanImplClassByBeanName.put(RoomRepository.class, RoomRepositoryJdbcImpl.class);
        beanImplClassByBeanName.put(BillRepository.class, BillRepositoryJdbcImpl.class);
        beanImplClassByBeanName.put(UserRepository.class, UserRepositoryJdbcImpl.class);
        beanImplClassByBeanName.put(ReservationRepository.class, ReservationRepositoryJdbcImpl.class);
        beanImplClassByBeanName.put(RoomClassRepository.class, RoomClassRepositoryJdbcImpl.class);

        //Other
    }

    @Override
    public Map<Object, Class<?>> getBeanImplClassByBeanName() {
        return beanImplClassByBeanName;
    }

    @Override
    public Map<Object, Object> getBeanByBeanName() {
        return beanByBeanName;
    }
}
