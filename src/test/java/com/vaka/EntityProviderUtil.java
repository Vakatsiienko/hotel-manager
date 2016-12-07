package com.vaka;

import com.vaka.domain.*;
import com.vaka.repository.BillRepository;
import com.vaka.repository.UserRepository;
import com.vaka.repository.ReservationRepository;
import com.vaka.repository.RoomRepository;
import com.vaka.context.ApplicationContext;
import com.vaka.util.DomainFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.*;
import java.time.temporal.ChronoUnit;
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
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC")).truncatedTo(ChronoUnit.SECONDS);
        room.setCreatedDatetime(now);
        room.setCapacity(4);
        room.setCostPerDay(200);
        room.setDescription("Description");
        room.setNumber(random.nextInt(400));
        room.setRoomClazz(RoomClass.values()[random.nextInt(RoomClass.values().length)]);
        return room;
    }


    public static Reservation createReservationWithoutRoom(){
        Reservation reservation = new Reservation();
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC")).truncatedTo(ChronoUnit.SECONDS);
        reservation.setCreatedDatetime(now);
        int arrivalYear = random.nextInt(2017) + 1;
        int arrivalMonth = random.nextInt(12) + 1;
        int arrivalDay = random.nextInt(25) + 1;
        reservation.setArrivalDate(LocalDate.of(arrivalYear, arrivalMonth, arrivalDay));
        reservation.setDepartureDate(LocalDate.of(arrivalYear + random.nextInt(2),
                arrivalMonth + random.nextInt(13 - arrivalMonth), arrivalDay + random.nextInt(26 - arrivalDay)));
        reservation.setRequestedRoomClass(RoomClass.STANDARD);
        reservation.setGuests(random.nextInt(10));
        reservation.setUser(userRepository.create(createUser()));
        reservation.setStatus(ReservationStatus.REQUESTED);
        return reservation;
    }

    public static User createUser() {
        User user = new User();
        Random random = new Random();
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC")).truncatedTo(ChronoUnit.SECONDS);
        user.setCreatedDatetime(now);
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
        Bill bill = DomainFactory.createBillFromReservation(reservation);
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC")).truncatedTo(ChronoUnit.SECONDS);
        bill.setCreatedDatetime(now);
        bill.setPaid(true);
        return bill;
    }
}
