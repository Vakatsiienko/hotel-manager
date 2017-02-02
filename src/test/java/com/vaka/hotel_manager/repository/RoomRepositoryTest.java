package com.vaka.hotel_manager.repository;

import com.vaka.hotel_manager.DBTestUtil;
import com.vaka.hotel_manager.EntityProviderUtil;
import com.vaka.hotel_manager.core.context.ApplicationContext;
import com.vaka.hotel_manager.domain.*;
import com.vaka.hotel_manager.domain.entities.Reservation;
import com.vaka.hotel_manager.domain.entities.Room;
import com.vaka.hotel_manager.domain.entities.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertThat;

/**
 * Created by Iaroslav on 12/3/2016.
 */
public class RoomRepositoryTest extends CrudRepositoryTest<Room> {
    private RoomRepository roomRepository = ApplicationContext.getInstance().getBean(RoomRepository.class);
    private ReservationRepository reservationRepository = ApplicationContext.getInstance().getBean(ReservationRepository.class);
    private RoomClassRepository roomClassRepository = ApplicationContext.getInstance().getBean(RoomClassRepository.class);
    private UserRepository userRepository = ApplicationContext.getInstance().getBean(UserRepository.class);

    @Before
    public void setUp() throws SQLException, ClassNotFoundException, IOException {
        DBTestUtil.reset();
    }

    @Test
    public void testFindAll() throws Exception {
        List<Room> expected = new ArrayList<>();
        Room room1 = roomRepository.create(createEntity());
        expected.add(room1);
        Room room2 = roomRepository.create(createEntity());
        expected.add(room2);
        Room room3 = roomRepository.create(createEntity());
        expected.add(room3);
        Room room4 = roomRepository.create(createEntity());
        expected.add(room4);
        Room room5 = roomRepository.create(createEntity());
        expected.add(room5);

        List<Room> actual = roomRepository.findAll();

        Assert.assertTrue(expected.size() == actual.size());
        assertThat(actual, hasItems(expected.toArray(new Room[expected.size()])));

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
                roomClassRepository.getByName("Standard").get(), LocalDate.of(2000, 1, 24), LocalDate.of(2000, 2, 10));

        Assert.assertTrue(expectedList.size() == actualList.size());
        Assert.assertTrue(actualList.containsAll(expectedList));
        notExpectedList.forEach(r -> Assert.assertFalse(actualList.contains(r)));
    }

    private List<Room> createRoomsWithUnfitReservations(){
        List<Room> notExpectedList = new ArrayList<>();
        Room notExpected1 = createEntity();
        notExpected1.setRoomClass(EntityProviderUtil.createOrGetStoredRoomClass("Standard"));
        notExpected1 = roomRepository.create(notExpected1);
        User user = userRepository.create(EntityProviderUtil.createUser());
        Reservation unfitReservation1 = EntityProviderUtil.createReservationWithoutRoom(EntityProviderUtil.createOrGetStoredRoomClass("Standard"), user);
        unfitReservation1.setRoom(notExpected1);
        unfitReservation1.setArrivalDate(LocalDate.of(2000, 1, 20));
        unfitReservation1.setDepartureDate(LocalDate.of(2000, 2, 1));
        unfitReservation1.setStatus(ReservationStatus.CONFIRMED);
        reservationRepository.create(unfitReservation1);
        reservationRepository.update(unfitReservation1.getId(), unfitReservation1);
        notExpectedList.add(notExpected1);

        //creating another overlap room and reservation
        Room notExpected2 = createEntity();
        notExpected2.setRoomClass(EntityProviderUtil.createOrGetStoredRoomClass("Standard"));
        notExpected2 = roomRepository.create(notExpected2);
        Reservation unfitReservation2 = EntityProviderUtil.createReservationWithoutRoom(EntityProviderUtil.createOrGetStoredRoomClass("Standard"), user);
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
        User user = userRepository.create(EntityProviderUtil.createUser());
        Room expected1 = createEntity();
        expected1.setRoomClass(EntityProviderUtil.createOrGetStoredRoomClass("Standard"));
        expected1 = roomRepository.create(expected1);
        Reservation suitReservation1 = EntityProviderUtil.createReservationWithoutRoom(EntityProviderUtil.createOrGetStoredRoomClass("Standard"), user);
        suitReservation1.setRoom(expected1);
        suitReservation1.setArrivalDate(LocalDate.of(2000, 1, 10));
        suitReservation1.setDepartureDate(LocalDate.of(2000, 1, 24));
        suitReservation1.setStatus(ReservationStatus.CONFIRMED);
        reservationRepository.create(suitReservation1);
        reservationRepository.update(suitReservation1.getId(), suitReservation1);
        Reservation suitReservation2 = EntityProviderUtil.createReservationWithoutRoom(EntityProviderUtil.createOrGetStoredRoomClass("Standard"), user);
        suitReservation2.setRoom(expected1);
        suitReservation2.setArrivalDate(LocalDate.of(2000, 2, 10));
        suitReservation2.setDepartureDate(LocalDate.of(2000, 2, 24));
        suitReservation2.setStatus(ReservationStatus.CONFIRMED);
        reservationRepository.create(suitReservation2);
        reservationRepository.update(suitReservation2.getId(), suitReservation2);
        expectedList.add(expected1);

        Room expected2 = createEntity();
        expected2.setRoomClass(EntityProviderUtil.createOrGetStoredRoomClass("Standard"));
        expected2 = roomRepository.create(expected2);
        Reservation suitReservation3 = EntityProviderUtil.createReservationWithoutRoom(EntityProviderUtil.createOrGetStoredRoomClass("Standard"), user);
        suitReservation3.setRoom(expected2);
        suitReservation3.setArrivalDate(LocalDate.of(2000, 1, 11));
        suitReservation3.setDepartureDate(LocalDate.of(2000, 1, 20));
        suitReservation3.setStatus(ReservationStatus.CONFIRMED);
        reservationRepository.create(suitReservation3);
        reservationRepository.update(suitReservation3.getId(), suitReservation3);
        Reservation suitReservation4 = EntityProviderUtil.createReservationWithoutRoom(EntityProviderUtil.createOrGetStoredRoomClass("Standard"), user);
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
        notExpected3.setRoomClass(EntityProviderUtil.createOrGetStoredRoomClass("Half Suite"));
        notExpected3 = roomRepository.create(notExpected3);
        notExpectedList.add(notExpected3);
        Room notExpected4 = createEntity();
        notExpected4.setRoomClass(EntityProviderUtil.createOrGetStoredRoomClass("King"));
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
        return EntityProviderUtil.createRoom(EntityProviderUtil.createOrGetStoredRoomClass("Standard"));
    }
}
