package com.vaka.hotel_manager.service;

import com.vaka.hotel_manager.EntityProviderUtil;
import com.vaka.hotel_manager.core.context.ApplicationContextHolder;
import com.vaka.hotel_manager.domain.entity.Reservation;
import com.vaka.hotel_manager.domain.entity.User;
import com.vaka.hotel_manager.repository.*;

import java.util.Optional;

import static org.mockito.Mockito.when;

/**
 * Created by Iaroslav on 12/16/2016.
 */
public class ReservationServiceTest extends CrudServiceTest<Reservation> {

    private ReservationRepository reservationRepositoryMock = ApplicationContextHolder.getContext().getBean(ReservationRepository.class);
    private ReservationService reservationService = ApplicationContextHolder.getContext().getBean(ReservationService.class);
    private UserRepository userRepositoryMock = ApplicationContextHolder.getContext().getBean(UserRepository.class);
    private RoomRepository roomRepositoryMock = ApplicationContextHolder.getContext().getBean(RoomRepository.class);
    private RoomClassRepository roomClassRepositoryMock = ApplicationContextHolder.getContext().getBean(RoomClassRepository.class);

    @Override
    protected CrudService<Reservation> getService() {
        return reservationService;
    }

    @Override
    protected CrudRepository<Reservation> getMockedRepository() {
        return reservationRepositoryMock;
    }
    @Override
    protected void beforeCreate() {
        when(roomClassRepositoryMock.getByName("Standard")).thenReturn(Optional.of(EntityProviderUtil.createRoomClass("Standard")));
    }
    @Override
    protected void beforeUpdate() {
        when(roomClassRepositoryMock.getByName("Standard")).thenReturn(Optional.of(EntityProviderUtil.createRoomClass("Standard")));
    }
    @Override
    protected Reservation createEntity() {
        User user = EntityProviderUtil.createUser();
        user.setId(2);
        return EntityProviderUtil.createReservationWithoutRoom(EntityProviderUtil.createRoomClass("Standard"), user);
    }
}
