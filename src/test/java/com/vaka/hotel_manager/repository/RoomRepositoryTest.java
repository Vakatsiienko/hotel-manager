package com.vaka.hotel_manager.repository;

import com.vaka.DBTestUtil;
import com.vaka.EntityProviderUtil;
import com.vaka.hotel_manager.domain.Reservation;
import com.vaka.hotel_manager.domain.ReservationStatus;
import com.vaka.hotel_manager.domain.Room;
import com.vaka.hotel_manager.domain.RoomClass;
import com.vaka.hotel_manager.context.ApplicationContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Iaroslav on 12/3/2016.
 */
public class RoomRepositoryTest extends CrudRepositoryTest<Room> {
    private RoomRepository roomRepository = ApplicationContext.getInstance().getBean(RoomRepository.class);
    private ReservationRepository reservationRepository = ApplicationContext.getInstance().getBean(ReservationRepository.class);

    @Before
    public void setUp() throws SQLException, ClassNotFoundException {
        DBTestUtil.reset();
    }

    @Test
    public void testFindAvailableForReservation() throws Exception {
        //Creating confirmed reservations and room that not conflict with our future request
        List<Room> expectedList = createRoomsWithSuitableReservations();

        //Creating rooms and reservation on them that overlap our request
        List<Room> notExpectedList = createRoomsWithUnfitReservations();

        //creating available rooms that not match RoomClass
        notExpectedList.addAll(creatingRoomsWithUnfitClass());


        List<Room> actualList = roomRepository.findAvailableForReservation(
                RoomClass.STANDARD, LocalDate.of(2000, 1, 24), LocalDate.of(2000, 2, 10));

        Assert.assertTrue(expectedList.size() == actualList.size());
        Assert.assertTrue(actualList.containsAll(expectedList));
        notExpectedList.forEach(r -> Assert.assertFalse(actualList.contains(r)));
    }

    private List<Room> createRoomsWithUnfitReservations(){
        List<Room> notExpectedList = new ArrayList<>();
        Room notExpected1 = createEntity();
        notExpected1.setRoomClazz(RoomClass.STANDARD);
        notExpected1 = roomRepository.create(notExpected1);
        Reservation unfitReservation1 = EntityProviderUtil.createReservationWithoutRoom();
        unfitReservation1.setRoom(notExpected1);
        unfitReservation1.setArrivalDate(LocalDate.of(2000, 1, 20));
        unfitReservation1.setDepartureDate(LocalDate.of(2000, 2, 1));
        unfitReservation1.setStatus(ReservationStatus.CONFIRMED);
        reservationRepository.create(unfitReservation1);
        reservationRepository.update(unfitReservation1.getId(), unfitReservation1);
        notExpectedList.add(notExpected1);

        //creating another overlap room and reservation
        Room notExpected2 = createEntity();
        notExpected2.setRoomClazz(RoomClass.STANDARD);
        notExpected2 = roomRepository.create(notExpected2);
        Reservation unfitReservation2 = EntityProviderUtil.createReservationWithoutRoom();
        unfitReservation2.setRoom(notExpected2);
        unfitReservation2.setArrivalDate(LocalDate.of(2000, 1, 1));
        unfitReservation2.setDepartureDate(LocalDate.of(2000, 1, 25));
        unfitReservation2.setStatus(ReservationStatus.CONFIRMED);
        reservationRepository.create(unfitReservation2);
        reservationRepository.update(unfitReservation2.getId(), unfitReservation2);
        notExpectedList.add(notExpected2);

        return notExpectedList;
    }

    private List<Room> createRoomsWithSuitableReservations(){
        List<Room> expectedList = new ArrayList<>();

        Room expected1 = createEntity();
        expected1.setRoomClazz(RoomClass.STANDARD);
        expected1 = roomRepository.create(expected1);
        Reservation suitReservation1 = EntityProviderUtil.createReservationWithoutRoom();
        suitReservation1.setRoom(expected1);
        suitReservation1.setArrivalDate(LocalDate.of(2000, 1, 10));
        suitReservation1.setDepartureDate(LocalDate.of(2000, 1, 24));
        suitReservation1.setStatus(ReservationStatus.CONFIRMED);
        reservationRepository.create(suitReservation1);
        reservationRepository.update(suitReservation1.getId(), suitReservation1);
        Reservation suitReservation2 = EntityProviderUtil.createReservationWithoutRoom();
        suitReservation2.setRoom(expected1);
        suitReservation2.setArrivalDate(LocalDate.of(2000, 2, 10));
        suitReservation2.setDepartureDate(LocalDate.of(2000, 2, 24));
        suitReservation2.setStatus(ReservationStatus.CONFIRMED);
        reservationRepository.create(suitReservation2);
        reservationRepository.update(suitReservation2.getId(), suitReservation2);
        expectedList.add(expected1);

        Room expected2 = createEntity();
        expected2.setRoomClazz(RoomClass.STANDARD);
        expected2 = roomRepository.create(expected2);
        Reservation suitReservation3 = EntityProviderUtil.createReservationWithoutRoom();
        suitReservation3.setRoom(expected2);
        suitReservation3.setArrivalDate(LocalDate.of(2000, 1, 11));
        suitReservation3.setDepartureDate(LocalDate.of(2000, 1, 20));
        suitReservation3.setStatus(ReservationStatus.CONFIRMED);
        reservationRepository.create(suitReservation3);
        reservationRepository.update(suitReservation3.getId(), suitReservation3);
        Reservation suitReservation4 = EntityProviderUtil.createReservationWithoutRoom();
        suitReservation4.setRoom(expected2);
        suitReservation4.setArrivalDate(LocalDate.of(2000, 2, 11));
        suitReservation4.setDepartureDate(LocalDate.of(2000, 2, 20));
        suitReservation4.setStatus(ReservationStatus.CONFIRMED);
        reservationRepository.create(suitReservation4);
        reservationRepository.update(suitReservation4.getId(), suitReservation4);
        expectedList.add(expected2);

        return expectedList;

    }

    private List<Room> creatingRoomsWithUnfitClass(){
        List<Room> notExpectedList = new ArrayList<>();
        Room notExpected3 = createEntity();
        notExpected3.setRoomClazz(RoomClass.HALF_SUITE);
        notExpected3 = roomRepository.create(notExpected3);
        notExpectedList.add(notExpected3);
        Room notExpected4 = createEntity();
        notExpected4.setRoomClazz(RoomClass.KING);
        notExpected4 = roomRepository.create(notExpected4);
        notExpectedList.add(notExpected4);
        return notExpectedList;
    }

    @Override
    protected CrudRepository<Room> getRepository() {
        return roomRepository;
    }

    @Override
    protected Room createEntity() {
        return EntityProviderUtil.createRoom();
    }
}
