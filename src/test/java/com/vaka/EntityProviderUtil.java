package com.vaka;

import com.vaka.domain.*;
import com.vaka.repository.CustomerRepository;
import com.vaka.repository.ReservationRepository;
import com.vaka.repository.RoomRepository;
import com.vaka.util.ApplicationContext;
import com.vaka.util.ServletDomainExtractor;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Base64;
import java.util.Random;

/**
 * Created by Iaroslav on 12/3/2016.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EntityProviderUtil {
    private static ReservationRepository reservationRepository = ApplicationContext.getBean(ReservationRepository.class);
    private static CustomerRepository customerRepository = ApplicationContext.getBean(CustomerRepository.class);
    private static RoomRepository roomRepository = ApplicationContext.getBean(RoomRepository.class);
    private static Random random = new Random();


    public static Room createRoom(){
        Room room = new Room();
        room.setCapacity(4);
        room.setCostPerDay(200);
        room.setDescription("Description");
        room.setNumber(random.nextInt(400));
        room.setRoomClazz(RoomClass.values()[random.nextInt(RoomClass.values().length)]);
        return room;
    }

    public static Reservation createReservation(){
        Reservation reservation = new Reservation();
        int arrivalYear = random.nextInt(2017) + 1;
        int arrivalMonth = random.nextInt(12) + 1;
        int arrivalDay = random.nextInt(25) + 1;
        reservation.setArrivalDate(LocalDate.of(arrivalYear, arrivalMonth, arrivalDay));
        reservation.setDepartureDate(LocalDate.of(arrivalYear + random.nextInt(2),
                arrivalMonth + random.nextInt(13 - arrivalMonth), arrivalDay + random.nextInt(26 - arrivalDay)));
        reservation.setRequestedRoomClass(RoomClass.FIRST_CLASS);
        reservation.setGuests(random.nextInt(10));
        reservation.setUser(customerRepository.create(new User("email@mail.m", "password", "name", Role.CUSTOMER, "+30987654321")));
        reservation.setStatus(ReservationStatus.REQUESTED);
        return reservation;
    }

    public static User createUser() {
        User user = new User();
        user.setName("name");
        user.setEmail("email@mail");
        user.setPhoneNumber("+398641423");
        user.setRole(Role.CUSTOMER);

        user.setPassword(Base64.getEncoder().encodeToString(String.join(":", user.getEmail(), user.getPhoneNumber()).getBytes()));
        return user;
    }

    public static Bill createBill() {
        Reservation reservation = createReservation();
        Room room = createRoom();
        reservation.setRoom(room);
        return ServletDomainExtractor.createFromReservation(reservation);
    }
}
