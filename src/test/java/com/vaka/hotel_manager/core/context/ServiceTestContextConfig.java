package com.vaka.hotel_manager.core.context;

import com.vaka.hotel_manager.core.context.config.ApplicationContextConfig;
import com.vaka.hotel_manager.repository.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Iaroslav on 12/16/2016.
 */
public class ServiceTestContextConfig extends ApplicationContextConfig {

    @Mock
    private BillRepository billRepositoryMock;

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private ReservationRepository reservationRepositoryMock;

    @Mock
    private RoomRepository roomRepositoryMock;

    @Mock
    private RoomClassRepository roomClassRepository;


    private Map<Object, Object> implBeanByName;

    {
        MockitoAnnotations.initMocks(this);
        implBeanByName = new HashMap<>();
        implBeanByName.put(BillRepository.class, billRepositoryMock);
        implBeanByName.put(UserRepository.class, userRepositoryMock);
        implBeanByName.put(ReservationRepository.class, reservationRepositoryMock);
        implBeanByName.put(RoomRepository.class, roomRepositoryMock);
        implBeanByName.put(RoomClassRepository.class, roomClassRepository);
        implBeanByName.keySet().forEach(getBeanImplClassByBeanName()::remove);
    }

    @Override
    public Map<Object, Object> getBeanByBeanName() {
        return implBeanByName;
    }
}
