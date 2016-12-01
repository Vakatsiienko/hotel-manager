package com.vaka.config;


import com.vaka.repository.*;
import com.vaka.repository.inMemoryImpl.*;
import com.vaka.service.*;
import com.vaka.service.impl.*;
import com.vaka.web.controller.*;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Iaroslav on 11/26/2016.
 */
@Getter
public class ApplicationContextConfig {
    private Map<Class<?>, Class<?>> implClassByBeanName;
    public ApplicationContextConfig(){
        implClassByBeanName = new HashMap<>();

        //Controllers
        implClassByBeanName.put(RoomController.class, RoomController.class);
        implClassByBeanName.put(BillController.class, BillController.class);
        implClassByBeanName.put(UserController.class, UserController.class);
        implClassByBeanName.put(ReservationController.class, ReservationController.class);
        implClassByBeanName.put(AuthenticationController.class, AuthenticationController.class);
        implClassByBeanName.put(MainController.class, MainController.class);

        //Services
        implClassByBeanName.put(RoomService.class, RoomServiceImpl.class);
        implClassByBeanName.put(BillService.class, BillServiceImpl.class);
        implClassByBeanName.put(ManagerService.class, ManagerServiceImpl.class);
        implClassByBeanName.put(CustomerService.class, CustomerServiceImpl.class);
        implClassByBeanName.put(SecurityService.class, SecurityServiceImpl.class);
        implClassByBeanName.put(ReservationService.class, ReservationServiceImpl.class);

        //Repository
        implClassByBeanName.put(RoomRepository.class, RoomRepositoryImpl.class);
        implClassByBeanName.put(BillRepository.class, BillRepositoryImpl.class);
        implClassByBeanName.put(ManagerRepository.class, ManagerRepositoryImpl.class);
        implClassByBeanName.put(CustomerRepository.class, CustomerRepositoryImpl.class);
        implClassByBeanName.put(SecurityRepository.class, SecurityRepositoryImpl.class);
        implClassByBeanName.put(ReservationRepository.class, ReservationRepositoryImpl.class);

        //Other
    }


}
