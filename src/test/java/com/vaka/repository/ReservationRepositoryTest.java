package com.vaka.repository;

import com.vaka.EntityProviderUtil;
import com.vaka.domain.Reservation;
import com.vaka.domain.ReservationStatus;
import com.vaka.domain.Room;
import com.vaka.util.ApplicationContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iaroslav on 02.12.16.
 */
public class ReservationRepositoryTest extends CrudRepositoryTest<Reservation> {
    private ReservationRepository reservationRepository = ApplicationContext.getBean(ReservationRepository.class);
    private CustomerRepository customerRepository = ApplicationContext.getBean(CustomerRepository.class);
    private RoomRepository roomRepository = ApplicationContext.getBean(RoomRepository.class);

    @Before
    public void setUp(){
        ApplicationContext.init();//TODO change to resetDB
    }
    @Test
    public void testFindByRoomId() {
        Room room = roomRepository.create(EntityProviderUtil.createRoom());
        //creating reservations with our room
        List<Reservation> reservationList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Reservation reservation = createEntity();
            reservation.setRoom(room);
            reservation.setStatus(ReservationStatus.CONFIRMED);
            reservationList.add(getRepository().create(reservation));
        }
        //creating reservations with other rooms
        List<Reservation> otherReservationList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Room otherRoom = roomRepository.create(EntityProviderUtil.createRoom());
            Reservation otherReservation = new Reservation();
            otherReservation.setRoom(otherRoom);
            otherReservation.setStatus(ReservationStatus.CONFIRMED);
            otherReservationList.add(reservationRepository.create(otherReservation));
        }

        List<Reservation> resultReservationList = reservationRepository.findConfirmedByRoomId(room.getId());

        Assert.assertTrue(resultReservationList.containsAll(reservationList));
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
            reservationRepository.create(reservation);;
        }
        for (int i = 0; i < 10; i++) {
            Reservation reservation = createEntity();
            reservation.setStatus(ReservationStatus.REJECTED);
            reservationRepository.create(reservation);
        }
        List<Reservation> resultList = reservationRepository.findConfirmed();
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
        List<Reservation> resultList = reservationRepository.findRequested();
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
            reservationRepository.create(reservation);;
        }
        for (int i = 0; i < 10; i++) {
            Reservation reservation = createEntity();
            reservation.setStatus(ReservationStatus.REJECTED);
            reservationRepository.create(reservation);
        }
        List<Reservation> resultList = reservationRepository.findRejected();
        Assert.assertTrue(resultList.size() == 10);
        resultList.forEach(r -> Assert.assertTrue(r.getStatus() == ReservationStatus.REJECTED));
    }


    @Override
    protected CrudRepository<Reservation> getRepository() {
        return reservationRepository;
    }

    @Override
    protected Reservation createEntity() {
        return EntityProviderUtil.createReservation();
    }
}
