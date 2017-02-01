package com.vaka.hotel_manager.repository;

import com.vaka.hotel_manager.DBTestUtil;
import com.vaka.hotel_manager.EntityProviderUtil;
import com.vaka.hotel_manager.core.context.ApplicationContext;
import com.vaka.hotel_manager.domain.DTO.ReservationDTO;
import com.vaka.hotel_manager.domain.Reservation;
import com.vaka.hotel_manager.domain.ReservationStatus;
import com.vaka.hotel_manager.domain.User;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;


/**
 * Created by iaroslav on 02.12.16.
 */
public class ReservationRepositoryTest extends CrudRepositoryTest<Reservation> {
    private ReservationRepository reservationRepository = ApplicationContext.getInstance().getBean(ReservationRepository.class);
    private RoomRepository roomRepository = ApplicationContext.getInstance().getBean(RoomRepository.class);
    private UserRepository userRepository = ApplicationContext.getInstance().getBean(UserRepository.class);
    private RoomClassRepository roomClassRepository = ApplicationContext.getInstance().getBean(RoomClassRepository.class);
//TODO write own connectionManageer

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
        reservation.setRoom(roomRepository.create(EntityProviderUtil.createRoom(EntityProviderUtil.createOrGetStoredRoomClass("Standard"))));
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservationRepository.update(reservation.getId(), reservation);

        Integer roomId = reservation.getRoom().getId();

        assertFalse(reservationRepository.existOverlapReservations(roomId, LocalDate.of(2016, 10, 1), LocalDate.of(2016, 10, 4)));
        assertTrue(reservationRepository.existOverlapReservations(roomId, LocalDate.of(2016, 10, 1), LocalDate.of(2016, 10, 5)));
        assertTrue(reservationRepository.existOverlapReservations(roomId, LocalDate.of(2016, 10, 5), LocalDate.of(2016, 10, 10)));
        assertTrue(reservationRepository.existOverlapReservations(roomId, LocalDate.of(2016, 10, 24), LocalDate.of(2016, 10, 29)));
        assertFalse(reservationRepository.existOverlapReservations(roomId, LocalDate.of(2016, 10, 25), LocalDate.of(2016, 10, 29)));
    }

    @Test
    public void testFindActiveByUserId() throws Exception {
        List<ReservationDTO> expectedList = new ArrayList<>();
        User u = userRepository.create(EntityProviderUtil.createUser());
        Reservation expected1 = createEntity();
        expected1.setUser(u);
        expected1.setStatus(ReservationStatus.REQUESTED);
        expected1.setDepartureDate(LocalDate.now().plusDays(25));
        reservationRepository.create(expected1);
        expectedList.add(EntityProviderUtil.toReservationDTO(expected1));

        Reservation expected2 = createEntity();
        expected2.setUser(u);
        expected2.setDepartureDate(LocalDate.now().plusDays(5));
        expected2.setStatus(ReservationStatus.CONFIRMED);
        reservationRepository.create(expected2);
        expected2.setRoom(roomRepository.create(EntityProviderUtil.createRoom(EntityProviderUtil.createOrGetStoredRoomClass("Standard"))));
        reservationRepository.update(expected2.getId(), expected2);
        expectedList.add(EntityProviderUtil.toReservationDTO(expected2));


        Reservation expected3 = createEntity();
        expected3.setUser(u);
        expected3.setDepartureDate(LocalDate.now());
        reservationRepository.create(expected3);
        expectedList.add(EntityProviderUtil.toReservationDTO(expected3));

        Reservation expected4 = createEntity();
        expected4.setUser(u);
        expected4.setDepartureDate(LocalDate.now().plusDays(5));
        expected4.setStatus(ReservationStatus.REJECTED);
        reservationRepository.create(expected4);
        expected4.setRoom(roomRepository.create(EntityProviderUtil.createRoom(EntityProviderUtil.createOrGetStoredRoomClass("Standard"))));
        reservationRepository.update(expected4.getId(), expected4);
        expectedList.add(EntityProviderUtil.toReservationDTO(expected4));

        //unfit reservations
        Reservation unexpected1 = createEntity();
        unexpected1.setUser(u);
        unexpected1.setStatus(ReservationStatus.REJECTED);
        unexpected1.setDepartureDate(LocalDate.now().minusDays(1));
        unexpected1.setArrivalDate(LocalDate.now().minusMonths(1));

        reservationRepository.create(unexpected1);

        Reservation unexpected2 = createEntity();
        unexpected2.setUser(u);
        unexpected2.setStatus(ReservationStatus.CONFIRMED);
        unexpected2.setDepartureDate(LocalDate.now().minusDays(1));
        unexpected2.setArrivalDate(LocalDate.now().minusWeeks(1));
        reservationRepository.create(createEntity());

        Reservation unexpected3 = createEntity();
        unexpected3.setStatus(ReservationStatus.REQUESTED);
        unexpected3.setDepartureDate(LocalDate.now().minusDays(20));
        unexpected3.setArrivalDate(LocalDate.now().minusMonths(1));
        reservationRepository.create(unexpected3);


        List<ReservationDTO> resultList = reservationRepository.findActiveByUserId(u.getId());

        assertTrue(resultList.size() == expectedList.size());
        assertThat(resultList, hasItems(expectedList.toArray(new ReservationDTO[expectedList.size()])));
        expectedList.stream().forEach(res -> assertTrue(resultList.contains(res)));
    }

    @Test
    public void testFindByUserIdAndStatus() {
        User user = userRepository.create(EntityProviderUtil.createUser());
        List<ReservationDTO> expectedList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Reservation reservation = createEntity();
            reservation.setUser(user);
            reservation.setStatus(ReservationStatus.REQUESTED);
            reservationRepository.create(reservation);
            expectedList.add(EntityProviderUtil.toReservationDTO(reservation));
        }
        for (int i = 0; i < 3; i++) {
            Reservation reservation = createEntity();
            reservation.setUser(user);
            reservation.setStatus(ReservationStatus.CONFIRMED);
            reservation = reservationRepository.create(reservation);
            reservation.setRoom(roomRepository.create(EntityProviderUtil.createRoom(EntityProviderUtil.createOrGetStoredRoomClass("Standard"))));
            reservationRepository.update(reservation.getId(), reservation);
            expectedList.add(EntityProviderUtil.toReservationDTO(reservation));
        }
        for (int i = 0; i < 3; i++) {
            reservationRepository.create(createEntity());
        }
        List<ReservationDTO> requestedList = reservationRepository.findByUserIdAndStatus(user.getId(), ReservationStatus.REQUESTED);
        List<ReservationDTO> confirmedList = reservationRepository.findByUserIdAndStatus(user.getId(), ReservationStatus.CONFIRMED);
        List<ReservationDTO> resultList = new ArrayList<>(requestedList.size() + confirmedList.size());
        resultList.addAll(requestedList);
        resultList.addAll(confirmedList);
        assertTrue(expectedList.size() == resultList.size());
        assertEquals(expectedList, resultList);
//        expectedList.forEach(reservation -> assertTrue(resultList.contains(reservation)));
        assertThat(expectedList, is(resultList));
    }
//    @Test
//    public void testFindByRoomIdAndStatus() {
//        Room room = roomRepository.create(EntityProviderUtil.createRoom());
//        //creating reservations with our room
//        List<ReservationDTO> reservationList = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            Reservation reservation = reservationRepository.create(createEntity());
//            reservation.setRoom(room);
//            reservation.setStatus(ReservationStatus.CONFIRMED);
//            reservationRepository.update(reservation.getId(), reservation);
//            reservationList.add(EntityProviderUtil.toReservationDTO(reservation));
//        }
//        //creating reservations with other rooms
//        List<ReservationDTO> otherReservationList = new ArrayList<>();
//        for (int i = 0; i < 15; i++) {
//            Room otherRoom = roomRepository.create(EntityProviderUtil.createRoom());
//            Reservation otherReservation = reservationRepository.create(createEntity());
//            otherReservation.setRoom(otherRoom);
//            otherReservation.setStatus(ReservationStatus.CONFIRMED);
//            reservationRepository.update(otherReservation.getId(), otherReservation);
//            otherReservationList.add(EntityProviderUtil.toReservationDTO(otherReservation));
//        }
//
//        List<ReservationDTO> resultReservationList = reservationRepository.findByRoomIdAndStatus(room.getId(), ReservationStatus.CONFIRMED);
//
//        resultReservationList.forEach(r -> assertTrue(r.getRoomId().equals(room.getId())));
//        resultReservationList.forEach(r -> assertTrue(r.getStatus() == ReservationStatus.CONFIRMED));
//        Assert.assertTrue(resultReservationList.containsAll(reservationList));
//        assertTrue(resultReservationList.size() == reservationList.size());
//        //test that none of other reservations contains in result
//        otherReservationList.forEach(r -> assertFalse(resultReservationList.contains(r)));
//    }

    @Test
    public void testFindByStatusFromDate() {
        for (int i = 0; i < 4; i++) {
            Reservation reservation = createEntity();
            reservation.setDepartureDate(LocalDate.now());
            reservation.setStatus(ReservationStatus.CONFIRMED);
            reservationRepository.create(reservation);
        }
        for (int i = 0; i < 4; i++) {
            Reservation reservation = createEntity();
            reservation.setDepartureDate(LocalDate.now());
            reservation.setStatus(ReservationStatus.REQUESTED);
            reservationRepository.create(reservation);
        }
        for (int i = 0; i < 4; i++) {
            Reservation reservation = createEntity();
            reservation.setDepartureDate(LocalDate.now());
            reservation.setStatus(ReservationStatus.REJECTED);
            reservationRepository.create(reservation);
        }
        List<ReservationDTO> confirmedList = reservationRepository.findPageByStatusFromDate(ReservationStatus.CONFIRMED, LocalDate.MIN, 1, 10).getContent();
        assertTrue(confirmedList.size() == 4);
        confirmedList.forEach(r -> assertTrue(r.getStatus() == ReservationStatus.CONFIRMED));

        List<ReservationDTO> requestedList = reservationRepository.findPageByStatusFromDate(ReservationStatus.REQUESTED, LocalDate.now(),  1, 10).getContent();
        assertTrue(requestedList.size() == 4);
        requestedList.forEach(r -> assertTrue(r.getStatus() == ReservationStatus.REQUESTED));

        List<ReservationDTO> rejectedList = reservationRepository.findPageByStatusFromDate(ReservationStatus.REJECTED, LocalDate.MIN,  1, 10).getContent();
        assertTrue(rejectedList.size() == 4);
        rejectedList.forEach(r -> assertTrue(r.getStatus() == ReservationStatus.REJECTED));

        List<ReservationDTO> fromFutureList = reservationRepository.findPageByStatusFromDate(ReservationStatus.REJECTED, LocalDate.now().plusDays(1), 1, 10).getContent();
        assertTrue(fromFutureList.size() == 0);

    }


    @Override
    protected CrudRepository<Reservation> getRepository() {
        return reservationRepository;
    }

    @Override
    protected Reservation createEntity() {
        return EntityProviderUtil.createReservationWithoutRoom(
                EntityProviderUtil.createOrGetStoredRoomClass("Standard"),
                userRepository.create(EntityProviderUtil.createUser()));
    }
}
