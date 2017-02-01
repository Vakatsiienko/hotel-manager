package com.vaka.hotel_manager.repository;

import com.vaka.hotel_manager.DBTestUtil;
import com.vaka.hotel_manager.EntityProviderUtil;
import com.vaka.hotel_manager.domain.*;
import com.vaka.hotel_manager.core.context.ApplicationContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Created by Iaroslav on 12/3/2016.
 */
public class BillRepositoryTest extends CrudRepositoryTest<Bill> {
    private BillRepository billRepository = ApplicationContext.getInstance().getBean(BillRepository.class);
    private RoomRepository roomRepository  = ApplicationContext.getInstance().getBean(RoomRepository.class);
    private ReservationRepository reservationRepository = ApplicationContext.getInstance().getBean(ReservationRepository.class);
    private RoomClassRepository roomClassRepository = ApplicationContext.getInstance().getBean(RoomClassRepository.class);
    private UserRepository userRepository = ApplicationContext.getInstance().getBean(UserRepository.class);

    @Before
    public void setUp() throws SQLException, ClassNotFoundException, IOException {
        DBTestUtil.reset();
    }
    @Test
    public void testGetByReservationId() throws Exception {
        Bill expected = createEntity();
        billRepository.create(expected);
        Optional<Bill> actual = billRepository.getByReservationId(expected.getReservation().getId());
        Assert.assertEquals(expected, actual.get());
    }

    @Override
    protected CrudRepository<Bill> getRepository() {
        return billRepository;
    }

    @Override
    protected Bill createEntity() {
        User user = userRepository.create(EntityProviderUtil.createUser());
        Room room = roomRepository.create(EntityProviderUtil.createRoom(EntityProviderUtil.createOrGetStoredRoomClass("Standard")));
        Reservation reservation = reservationRepository.create(EntityProviderUtil.createReservationWithoutRoom(EntityProviderUtil.createOrGetStoredRoomClass("Standard"), user));
        reservation.setRoom(room);
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservationRepository.update(reservation.getId(), reservation);
        return EntityProviderUtil.createBill(reservation);
    }
}
