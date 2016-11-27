package com.vaka.config;


import com.vaka.repository.*;
import com.vaka.repository.inMemoryImpl.*;
import com.vaka.service.*;
import com.vaka.service.impl.*;
import com.vaka.web.controller.BillController;
import com.vaka.web.controller.ReservationRequestController;
import com.vaka.web.controller.RoomController;
import com.vaka.web.controller.UserController;
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
        implClassByBeanName.put(ReservationRequestController.class, ReservationRequestController.class);
        implClassByBeanName.put(RoomController.class, RoomController.class);
        implClassByBeanName.put(BillController.class, BillController.class);
        implClassByBeanName.put(UserController.class, UserController.class);

        //Services
        implClassByBeanName.put(RoomService.class, RoomServiceImpl.class);
        implClassByBeanName.put(BillService.class, BillServiceImpl.class);
        implClassByBeanName.put(ReservationRequestService.class, ReservationRequestServiceImpl.class);
        implClassByBeanName.put(UserService.class, UserServiceImpl.class);
        implClassByBeanName.put(SecurityService.class, SecurityServiceImpl.class);
        implClassByBeanName.put(ReservationService.class, ReservationServiceImpl.class);

        //Repository
        implClassByBeanName.put(RoomRepository.class, RoomRepositoryImpl.class);
        implClassByBeanName.put(BillRepository.class, BillRepositoryImpl.class);
        implClassByBeanName.put(ReservationRequestRepository.class, ReservationRequestRepositoryImpl.class);
        implClassByBeanName.put(UserRepository.class, UserRepositoryImpl.class);
        implClassByBeanName.put(SecurityRepository.class, SecurityRepositoryImpl.class);
        implClassByBeanName.put(ReservationRepository.class, ReservationRepositoryImpl.class);

        //Other
    }


}
