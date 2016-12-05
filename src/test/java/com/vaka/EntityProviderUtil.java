package com.vaka;

import com.vaka.domain.*;
import com.vaka.repository.BillRepository;
import com.vaka.repository.UserRepository;
import com.vaka.repository.ReservationRepository;
import com.vaka.repository.RoomRepository;
import com.vaka.context.ApplicationContext;
import com.vaka.util.DomainExtractor;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Random;

/**
 * Created by Iaroslav on 12/3/2016.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EntityProviderUtil {
    private static ReservationRepository reservationRepository = ApplicationContext.getInstance().getBean(ReservationRepository.class);
    private static UserRepository userRepository = ApplicationContext.getInstance().getBean(UserRepository.class);
    private static RoomRepository roomRepository = ApplicationContext.getInstance().getBean(RoomRepository.class);
    private static BillRepository billRepository = ApplicationContext.getInstance().getBean(BillRepository.class);
    private static Random random = new Random();


    public static Room createRoom(){
        Room room = new Room();
        Timestamp timestamp = Timestamp.from(Instant.now());
        timestamp.setNanos(0);
        room.setCreatedDatetime(timestamp.toLocalDateTime());
        room.setCapacity(4);
        room.setCostPerDay(200);
        room.setDescription("Description");
        room.setNumber(random.nextInt(400));
        room.setRoomClazz(RoomClass.values()[random.nextInt(RoomClass.values().length)]);
        return room;
    }


    public static Reservation createReservationWithoutRoom(){
        Reservation reservation = new Reservation();
        Timestamp timestamp = Timestamp.from(Instant.now());
        timestamp.setNanos(0);;
        reservation.setCreatedDatetime(timestamp.toLocalDateTime());

        int arrivalYear = random.nextInt(2017) + 1;
        int arrivalMonth = random.nextInt(12) + 1;
        int arrivalDay = random.nextInt(25) + 1;
        reservation.setArrivalDate(LocalDate.of(arrivalYear, arrivalMonth, arrivalDay));
        reservation.setDepartureDate(LocalDate.of(arrivalYear + random.nextInt(2),
                arrivalMonth + random.nextInt(13 - arrivalMonth), arrivalDay + random.nextInt(26 - arrivalDay)));
        reservation.setRequestedRoomClass(RoomClass.FIRST_CLASS);
        reservation.setGuests(random.nextInt(10));
        reservation.setUser(userRepository.create(createUser()));
        reservation.setStatus(ReservationStatus.REQUESTED);
        return reservation;
    }

    public static User createUser() {
        User user = new User();
        Random random = new Random();
        Timestamp timestamp = Timestamp.from(Instant.now());
        timestamp.setNanos(0);
        user.setCreatedDatetime(timestamp.toLocalDateTime());
        user.setName("name");
        user.setEmail(random.nextInt(100000) + "goodmanmen@gmail.com" + random.nextInt(100000));
        user.setPhoneNumber("+398641423");
        user.setRole(Role.CUSTOMER);

        user.setPassword(Base64.getEncoder().encodeToString(String.join(":", user.getEmail(), user.getPhoneNumber()).getBytes()));
        return user;
    }

    public static Bill createBill() {
        Reservation reservation = createReservationWithoutRoom();
        reservation = reservationRepository.create(reservation);
        reservation.setRoom(roomRepository.create(createRoom()));
        reservationRepository.update(reservation.getId(), reservation);
        Bill bill = DomainExtractor.createBillFromReservation(reservation);
        Timestamp timestamp = Timestamp.from(Instant.now());
        timestamp.setNanos(0);

        bill.setCreatedDatetime(timestamp.toLocalDateTime());
        bill.setPaid(true);
        return bill;
    }
}
