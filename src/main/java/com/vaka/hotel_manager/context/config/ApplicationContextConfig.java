package com.vaka.hotel_manager.context.config;


import com.vaka.hotel_manager.repository.*;
import com.vaka.hotel_manager.repository.inMemoryImpl.SecurityRepositoryImpl;
import com.vaka.hotel_manager.repository.jdbcImpl.BillRepositoryJdbcImpl;
import com.vaka.hotel_manager.repository.jdbcImpl.ReservationRepositoryJdbcImpl;
import com.vaka.hotel_manager.repository.jdbcImpl.RoomRepositoryJdbcImpl;
import com.vaka.hotel_manager.repository.jdbcImpl.UserRepositoryJdbcImpl;
import com.vaka.hotel_manager.service.*;
import com.vaka.hotel_manager.service.impl.*;
import com.vaka.hotel_manager.web.controller.*;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Iaroslav on 11/26/2016.
 */
@Getter
public class ApplicationContextConfig {
    private Map<Class<?>, Class<?>> implClassByBeanName;

    public ApplicationContextConfig() {
        implClassByBeanName = new HashMap<>();

        //Controllers
        implClassByBeanName.put(RoomController.class, RoomController.class);
        implClassByBeanName.put(BillController.class, BillController.class);
        implClassByBeanName.put(UserController.class, UserController.class);
        implClassByBeanName.put(ReservationController.class, ReservationController.class);

        //Services
        implClassByBeanName.put(RoomService.class, RoomServiceImpl.class);
        implClassByBeanName.put(BillService.class, BillServiceImpl.class);
        implClassByBeanName.put(UserService.class, UserServiceImpl.class);
        implClassByBeanName.put(SecurityService.class, SecurityServiceImpl.class);
        implClassByBeanName.put(ReservationService.class, ReservationServiceImpl.class);

        //Repository
        implClassByBeanName.put(RoomRepository.class, RoomRepositoryJdbcImpl.class);
        implClassByBeanName.put(BillRepository.class, BillRepositoryJdbcImpl.class);
        implClassByBeanName.put(UserRepository.class, UserRepositoryJdbcImpl.class);
        implClassByBeanName.put(SecurityRepository.class, SecurityRepositoryImpl.class);
        implClassByBeanName.put(ReservationRepository.class, ReservationRepositoryJdbcImpl.class);

        //Other
    }


}
