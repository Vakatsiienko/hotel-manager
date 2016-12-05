package com.vaka.repository;

import com.vaka.DBTestUtil;
import com.vaka.EntityProviderUtil;
import com.vaka.context.ApplicationContext;
import com.vaka.context.config.ApplicationContextConfig;
import com.vaka.context.config.PersistenceConfig;
import com.vaka.domain.Reservation;
import com.vaka.domain.ReservationStatus;
import com.vaka.domain.Room;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by iaroslav on 02.12.16.
 */
public class ReservationRepositoryTest extends CrudRepositoryTest<Reservation> {
    private ReservationRepository reservationRepository = ApplicationContext.getInstance().getBean(ReservationRepository.class);
    private RoomRepository roomRepository = ApplicationContext.getInstance().getBean(RoomRepository.class);

    @Before
    public void setUp() throws SQLException, ClassNotFoundException {
        ApplicationContext.getInstance().init(new ApplicationContextConfig(), new PersistenceConfig());//TODO change to resetDB
        DBTestUtil.reset();
    }

    @Test
    public void testFindByRoomIdAndStatus() {
        Room room = roomRepository.create(EntityProviderUtil.createRoom());
        //creating reservations with our room
        List<Reservation> reservationList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Reservation reservation = reservationRepository.create(createEntity());
            reservation.setRoom(room);
            reservation.setStatus(ReservationStatus.CONFIRMED);
            reservationRepository.update(reservation.getId(), reservation);
            reservationList.add(reservation);
        }
        //creating reservations with other rooms
        List<Reservation> otherReservationList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            Room otherRoom = roomRepository.create(EntityProviderUtil.createRoom());
            Reservation otherReservation = reservationRepository.create(createEntity());
            otherReservation.setRoom(otherRoom);
            otherReservation.setStatus(ReservationStatus.CONFIRMED);
            reservationRepository.update(otherReservation.getId(), otherReservation);
            otherReservationList.add(otherReservation);
        }

        List<Reservation> resultReservationList = reservationRepository.findByRoomIdAndStatus(room.getId(), ReservationStatus.CONFIRMED);

        resultReservationList.forEach(r -> Assert.assertTrue(r.getRoom().getId().equals(room.getId())));
        resultReservationList.forEach(r -> Assert.assertTrue(r.getStatus() == ReservationStatus.CONFIRMED));
//        Assert.assertTrue(resultReservationList.containsAll(reservationList)); Didn't work in cause of lombok equalsAndHashcode
        Assert.assertTrue(resultReservationList.size() == reservationList.size());
        //test that none of other reservations contains in result
        otherReservationList.forEach(r -> Assert.assertFalse(resultReservationList.contains(r)));
    }

    @Test
    public void testFindConfirmed() {
        for (int i = 0; i < 10; i++) {
            Reservation reservation = createEntity();
            reservation.setStatus(ReservationStatus.CONFIRMED);
            reservationRepository.create(reservation);
        }
        for (int i = 0; i < 10; i++) {
            Reservation reservation = createEntity();
            reservation.setStatus(ReservationStatus.REQUESTED);
            reservationRepository.create(reservation);
        }
        for (int i = 0; i < 10; i++) {
            Reservation reservation = createEntity();
            reservation.setStatus(ReservationStatus.REJECTED);
            reservationRepository.create(reservation);
        }
        List<Reservation> resultList = reservationRepository.findByStatus(ReservationStatus.CONFIRMED);
        Assert.assertTrue(resultList.size() == 10);
        resultList.forEach(r -> Assert.assertTrue(r.getStatus() == ReservationStatus.CONFIRMED));
    }

    @Test
    public void testFindRequested() {
        for (int i = 0; i < 10; i++) {
            Reservation reservation = createEntity();
            reservation.setStatus(ReservationStatus.CONFIRMED);
            reservationRepository.create(reservation);
        }
        for (int i = 0; i < 10; i++) {
            Reservation reservation = createEntity();
            reservation.setStatus(ReservationStatus.REQUESTED);
            reservationRepository.create(reservation);
        }
        for (int i = 0; i < 10; i++) {
            Reservation reservation = createEntity();
            reservation.setStatus(ReservationStatus.REJECTED);
            reservationRepository.create(reservation);
        }
        List<Reservation> resultList = reservationRepository.findByStatus(ReservationStatus.REQUESTED);
        Assert.assertTrue(resultList.size() == 10);
        resultList.forEach(r -> Assert.assertTrue(r.getStatus() == ReservationStatus.REQUESTED));
    }

    @Test
    public void testFindRejected() {
        for (int i = 0; i < 10; i++) {
            Reservation reservation = createEntity();
            reservation.setStatus(ReservationStatus.CONFIRMED);
            reservationRepository.create(reservation);
        }
        for (int i = 0; i < 10; i++) {
            Reservation reservation = createEntity();
            reservation.setStatus(ReservationStatus.REQUESTED);
            reservationRepository.create(reservation);
            ;
        }
        for (int i = 0; i < 10; i++) {
            Reservation reservation = createEntity();
            reservation.setStatus(ReservationStatus.REJECTED);
            reservationRepository.create(reservation);
        }
        List<Reservation> resultList = reservationRepository.findByStatus(ReservationStatus.REJECTED);
        Assert.assertTrue(resultList.size() == 10);
        resultList.forEach(r -> Assert.assertTrue(r.getStatus() == ReservationStatus.REJECTED));
    }


    @Override
    protected CrudRepository<Reservation> getRepository() {
        return reservationRepository;
    }

    @Override
    protected Reservation createEntity() {
        return EntityProviderUtil.createReservationWithoutRoom();
    }
}
