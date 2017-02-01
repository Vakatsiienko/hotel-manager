package com.vaka.hotel_manager;

import com.vaka.hotel_manager.core.context.ApplicationContext;
import com.vaka.hotel_manager.domain.*;
import com.vaka.hotel_manager.domain.DTO.ReservationDTO;
import com.vaka.hotel_manager.domain.DTO.RoomClassDTO;
import com.vaka.hotel_manager.repository.*;
import com.vaka.hotel_manager.util.DomainFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
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
    private static RoomClassRepository roomClassRepository = ApplicationContext.getInstance().getBean(RoomClassRepository.class);
    private static Random random = new Random();

    private static int roomNumber = 1;
    private static int emailNumber = 1;

    public static RoomClass createRoomClass(String name) {
        RoomClass roomClass = new RoomClass();
        roomClass.setCreatedDatetime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        roomClass.setName(name);
        return roomClass;
    }

    public static RoomClass createOrGetStoredRoomClass(String name) {
        Optional<RoomClass> rcOpt = roomClassRepository.getByName(name);
        if (rcOpt.isPresent())
            return rcOpt.get();
        else return roomClassRepository.create(createRoomClass(name));
    }


    public static Room createRoom(RoomClass roomClass) {
        Room room = new Room();
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        room.setCreatedDatetime(now);
        room.setCapacity(4);
        room.setCostPerDay(200);
        room.setNumber(roomNumber++);
        room.setRoomClass(roomClass);
        return room;
    }


    public static Reservation createReservationWithoutRoom(RoomClass requestedRoomClass, User user) {
        Reservation reservation = new Reservation();
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        reservation.setCreatedDatetime(now);
        int arrivalYear = random.nextInt(2017) + 1;
        int arrivalMonth = random.nextInt(12) + 1;
        int arrivalDay = random.nextInt(25) + 1;
        reservation.setArrivalDate(LocalDate.of(arrivalYear, arrivalMonth, arrivalDay));
        reservation.setDepartureDate(LocalDate.of(arrivalYear + random.nextInt(2),
                arrivalMonth + random.nextInt(13 - arrivalMonth), arrivalDay + random.nextInt(26 - arrivalDay)));
        reservation.setRequestedRoomClass(requestedRoomClass);
        reservation.setGuests(random.nextInt(10));
        reservation.setUser(user);
        reservation.setStatus(ReservationStatus.REQUESTED);
        return reservation;
    }

    public static User createUser() {
        User user = new User();
        Random random = new Random();
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        user.setCreatedDatetime(now);
        user.setName("name");
        user.setEmail("goodmanmen@gmail.com" + emailNumber++);
        user.setPhoneNumber("+398641423");
        user.setRole(Role.CUSTOMER);

        user.setPassword(BCrypt.hashpw(String.join(":", user.getName(), user.getPhoneNumber()), BCrypt.gensalt()));
        return user;
    }

    public static Bill createBill(Reservation reservation) {
        Bill bill = DomainFactory.createBillFromReservation(reservation);
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        bill.setCreatedDatetime(now);
        bill.setPaid(true);
        return bill;
    }

    public static ReservationDTO toReservationDTO(Reservation reservation) {
        Integer roomId = null;
        if (reservation.getRoom() != null)
            roomId = reservation.getRoom().getId();
        return new ReservationDTO(reservation.getId(), reservation.getCreatedDatetime(), reservation.getUser().getId(),
                roomId, reservation.getGuests(), reservation.getStatus(), toRoomClassDTO(reservation.getRequestedRoomClass()),
                reservation.getArrivalDate(), reservation.getDepartureDate());
    }

    public static RoomClassDTO toRoomClassDTO(RoomClass roomClass) {
        return new RoomClassDTO(roomClass.getName());
    }
}
