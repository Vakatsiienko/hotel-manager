package com.vaka.hotel_manager.service;

import com.vaka.hotel_manager.EntityProviderUtil;
import com.vaka.hotel_manager.core.context.ApplicationContext;
import com.vaka.hotel_manager.domain.Reservation;
import com.vaka.hotel_manager.domain.User;
import com.vaka.hotel_manager.repository.CrudRepository;
import com.vaka.hotel_manager.repository.ReservationRepository;
import com.vaka.hotel_manager.repository.RoomRepository;
import com.vaka.hotel_manager.repository.UserRepository;

/**
 * Created by Iaroslav on 12/16/2016.
 */
public class ReservationServiceTest extends CrudServiceTest<Reservation> {

    private ReservationRepository reservationRepositoryMock = ApplicationContext.getInstance().getBean(ReservationRepository.class);
    private ReservationService reservationService = ApplicationContext.getInstance().getBean(ReservationService.class);
    private UserRepository userRepositoryMock = ApplicationContext.getInstance().getBean(UserRepository.class);
    private RoomRepository roomRepositoryMock = ApplicationContext.getInstance().getBean(RoomRepository.class);

    @Override
    protected CrudService<Reservation> getService() {
        return reservationService;
    }

    @Override
    protected CrudRepository<Reservation> getMockedRepository() {
        return reservationRepositoryMock;
    }

    @Override
    protected Reservation createEntity() {
        Reservation reservation = EntityProviderUtil.createReservationWithoutRoom();
        User user = EntityProviderUtil.createUser();
        user.setId(2);
        reservation.setUser(user);
        return reservation;
    }
}
