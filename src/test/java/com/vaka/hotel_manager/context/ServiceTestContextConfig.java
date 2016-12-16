package com.vaka.hotel_manager.context;

import com.vaka.hotel_manager.context.config.ApplicationContextConfig;
import com.vaka.hotel_manager.repository.BillRepository;
import com.vaka.hotel_manager.repository.ReservationRepository;
import com.vaka.hotel_manager.repository.RoomRepository;
import com.vaka.hotel_manager.repository.UserRepository;
import org.mockito.InjectMocks;
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

    private Map<Class<?>, Object> implBeanByName;
    {
        MockitoAnnotations.initMocks(this);
        implBeanByName = new HashMap<>();
        implBeanByName.put(BillRepository.class, billRepositoryMock);
        implBeanByName.put(UserRepository.class, userRepositoryMock);
        implBeanByName.put(ReservationRepository.class, reservationRepositoryMock);
        implBeanByName.put(RoomRepository.class, roomRepositoryMock);

        implBeanByName.keySet().forEach(getImplClassByBeanName()::remove);
    }

    @Override
    public Map<Class<?>, Class<?>> getImplClassByBeanName() {
        return super.getImplClassByBeanName();
    }

    @Override
    public Map<Class<?>, Object> getImplBeanByBeanName() {
        return implBeanByName;
    }
}
