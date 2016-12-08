package com.vaka.hotel_manager.repository;

import com.vaka.hotel_manager.DBTestUtil;
import com.vaka.hotel_manager.EntityProviderUtil;
import com.vaka.hotel_manager.context.ApplicationContext;
import com.vaka.hotel_manager.domain.Reservation;
import com.vaka.hotel_manager.domain.ReservationStatus;
import com.vaka.hotel_manager.domain.Room;
import com.vaka.hotel_manager.domain.User;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Created by iaroslav on 02.12.16.
 */
public class ReservationRepositoryTest extends CrudRepositoryTest<Reservation> {
    private ReservationRepository reservationRepository = ApplicationContext.getInstance().getBean(ReservationRepository.class);
    private RoomRepository roomRepository = ApplicationContext.getInstance().getBean(RoomRepository.class);
    private UserRepository userRepository = ApplicationContext.getInstance().getBean(UserRepository.class);

    @Before
    public void setUp() throws SQLException, ClassNotFoundException, IOException {
        DBTestUtil.reset();
    }

    @Test
    public void testExistOverlapReservation() throws Exception {
        Reservation reservation = createEntity();
        reservation.setDepartureDate(LocalDate.of(2016, 10, 25));
        reservation.setArrivalDate(LocalDate.of(2016, 10, 4));
        reservationRepository.create(reservation);
        reservation.setRoom(roomRepository.create(EntityProviderUtil.createRoom()));
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservationRepository.update(reservation.getId(), reservation);

        Integer roomId = reservation.getRoom().getId();

        assertFalse(reservationRepository.existOverlapReservation(roomId, LocalDate.of(2016, 10, 1), LocalDate.of(2016, 10, 4)));
        assertTrue(reservationRepository.existOverlapReservation(roomId, LocalDate.of(2016, 10, 1), LocalDate.of(2016, 10, 5)));
        assertTrue(reservationRepository.existOverlapReservation(roomId, LocalDate.of(2016, 10, 5), LocalDate.of(2016, 10, 10)));
        assertTrue(reservationRepository.existOverlapReservation(roomId, LocalDate.of(2016, 10, 24), LocalDate.of(2016, 10, 29)));
        assertFalse(reservationRepository.existOverlapReservation(roomId, LocalDate.of(2016, 10, 25), LocalDate.of(2016, 10, 29)));
    }

    @Test
    public void testFindActiveByUserId() throws Exception {
        List<Reservation> expectedList = new ArrayList<>();
        User u = userRepository.create(EntityProviderUtil.createUser());
        Reservation expected1 = createEntity();
        expected1.setUser(u);
        expected1.setStatus(ReservationStatus.REQUESTED);
        expected1.setDepartureDate(LocalDate.now().plusDays(25));
        reservationRepository.create(expected1);
        expectedList.add(expected1);

        Reservation expected2 = createEntity();
        expected2.setUser(u);
        expected2.setDepartureDate(LocalDate.now().plusDays(5));
        expected2.setStatus(ReservationStatus.CONFIRMED);
        reservationRepository.create(expected2);
        expected2.setRoom(roomRepository.create(EntityProviderUtil.createRoom()));
        reservationRepository.update(expected2.getId(), expected2);
        expectedList.add(expected2);


        Reservation expected3 = createEntity();
        expected3.setUser(u);
        expected3.setDepartureDate(LocalDate.now());
        reservationRepository.create(expected3);
        expectedList.add(expected3);

        //unfit reservations
        List<Reservation> unExpectedList = new ArrayList<>();
        Reservation unexpected1 = createEntity();
        unexpected1.setUser(u);
        unexpected1.setStatus(ReservationStatus.REJECTED);
        unexpected1.setDepartureDate(LocalDate.now().plusMonths(1));
        unexpected1.setArrivalDate(LocalDate.now().plusDays(1));

        unExpectedList.add(reservationRepository.create(unexpected1));

        Reservation unexpected2 = createEntity();
        unexpected2.setUser(u);
        unexpected2.setStatus(ReservationStatus.CONFIRMED);
        unexpected2.setDepartureDate(LocalDate.now().minusDays(1));
        unexpected2.setArrivalDate(LocalDate.now().minusWeeks(1));
        unExpectedList.add(reservationRepository.create(createEntity()));

        Reservation unexpected3 = createEntity();
        unexpected2.setStatus(ReservationStatus.CONFIRMED);
        unexpected2.setDepartureDate(LocalDate.now().plusDays(20));
        unexpected2.setArrivalDate(LocalDate.now().plusDays(1));
        unExpectedList.add(reservationRepository.create(createEntity()));


        List<Reservation> resultList = reservationRepository.findActiveByUserId(u.getId());

        assertTrue(resultList.size() == expectedList.size());
        unExpectedList.forEach(unexpected -> assertTrue(!resultList.contains(unexpected)));
        assertThat(expectedList, is(resultList));
        assertThat(resultList, hasItems(expectedList.toArray(new Reservation[expectedList.size()])));
        expectedList.stream().forEach(res -> assertTrue(resultList.contains(res)));
    }

    @Test
    public void testFindByUserIdAndStatus() {
        User user = userRepository.create(EntityProviderUtil.createUser());
        List<Reservation> expectedList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Reservation reservation = createEntity();
            reservation.setUser(user);
            reservation.setStatus(ReservationStatus.REQUESTED);
            reservationRepository.create(reservation);
            expectedList.add(reservation);
        }
        for (int i = 0; i < 3; i++) {
            Reservation reservation = createEntity();
            reservation.setUser(user);
            reservation.setStatus(ReservationStatus.CONFIRMED);
            reservation = reservationRepository.create(reservation);
            reservation.setRoom(roomRepository.create(EntityProviderUtil.createRoom()));
            reservationRepository.update(reservation.getId(), reservation);
            expectedList.add(reservation);
        }
        for (int i = 0; i < 3; i++) {
            reservationRepository.create(createEntity());
        }
        List<Reservation> requestedList = reservationRepository.findByUserIdAndStatus(user.getId(), ReservationStatus.REQUESTED);
        List<Reservation> confirmedList = reservationRepository.findByUserIdAndStatus(user.getId(), ReservationStatus.CONFIRMED);
        List<Reservation> resultList = new ArrayList<>(requestedList.size() + confirmedList.size());
        resultList.addAll(requestedList);
        resultList.addAll(confirmedList);
        assertTrue(expectedList.size() == resultList.size());
        resultList.forEach(reservation -> assertTrue(resultList.contains(reservation)));
        assertThat(expectedList, is(resultList));
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

        resultReservationList.forEach(r -> assertTrue(r.getRoom().getId().equals(room.getId())));
        resultReservationList.forEach(r -> assertTrue(r.getStatus() == ReservationStatus.CONFIRMED));
//        Assert.assertTrue(resultReservationList.containsAll(reservationList)); Didn't work in cause of lombok equalsAndHashcode
        assertTrue(resultReservationList.size() == reservationList.size());
        //test that none of other reservations contains in result
        otherReservationList.forEach(r -> assertFalse(resultReservationList.contains(r)));
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
        assertTrue(resultList.size() == 10);
        resultList.forEach(r -> assertTrue(r.getStatus() == ReservationStatus.CONFIRMED));
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
        assertTrue(resultList.size() == 10);
        resultList.forEach(r -> assertTrue(r.getStatus() == ReservationStatus.REQUESTED));
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
        assertTrue(resultList.size() == 10);
        resultList.forEach(r -> assertTrue(r.getStatus() == ReservationStatus.REJECTED));
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