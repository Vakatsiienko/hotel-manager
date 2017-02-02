package com.vaka.hotel_manager.core.context;

import com.vaka.hotel_manager.core.context.config.ApplicationContextConfig;
import com.vaka.hotel_manager.core.tx.TestTransactionHelper;
import com.vaka.hotel_manager.core.tx.TestTransactionManager;
import com.vaka.hotel_manager.core.tx.TransactionHelper;
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


    private Map<Class<?>, Object> implBeanByName;
    {
        MockitoAnnotations.initMocks(this);
        implBeanByName = new HashMap<>();
        implBeanByName.put(BillRepository.class, billRepositoryMock);
        implBeanByName.put(UserRepository.class, userRepositoryMock);
        implBeanByName.put(ReservationRepository.class, reservationRepositoryMock);
        implBeanByName.put(RoomRepository.class, roomRepositoryMock);
        implBeanByName.put(RoomClassRepository.class, roomClassRepository);
        implBeanByName.put(TransactionHelper.class, new TestTransactionHelper(new TestTransactionManager()));
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
