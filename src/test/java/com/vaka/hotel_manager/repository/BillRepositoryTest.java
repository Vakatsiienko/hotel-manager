package com.vaka.hotel_manager.repository;

import com.vaka.hotel_manager.EntityProviderUtil;
import com.vaka.hotel_manager.domain.Bill;
import com.vaka.hotel_manager.core.context.ApplicationContext;
import com.vaka.hotel_manager.domain.Reservation;
import com.vaka.hotel_manager.domain.Room;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

/**
 * Created by Iaroslav on 12/3/2016.
 */
public class BillRepositoryTest extends CrudRepositoryTest<Bill> {
    private BillRepository billRepository = ApplicationContext.getInstance().getBean(BillRepository.class);
    private RoomRepository roomRepository  = ApplicationContext.getInstance().getBean(RoomRepository.class);
    private ReservationRepository reservationRepository = ApplicationContext.getInstance().getBean(ReservationRepository.class);


    @Test
    public void testGetByReservationId() throws Exception {
        Room room = roomRepository.create(EntityProviderUtil.createRoom());
        Reservation reservation = reservationRepository.create(EntityProviderUtil.createReservationWithoutRoom());
        reservation.setRoom(room);
        reservationRepository.update(reservation.getId(), reservation);
        Bill bill = createEntity();
        bill.setReservation(reservation);
        billRepository.create(bill);
        Optional<Bill> expected = billRepository.getById(bill.getId());
        Optional<Bill> actual = billRepository.getByReservationId(reservation.getId());
        Assert.assertEquals(expected.get(), actual.get());
    }

    @Override
    protected CrudRepository<Bill> getRepository() {
        return billRepository;
    }

    @Override
    protected Bill createEntity() {
        return EntityProviderUtil.createBill();
    }
}
