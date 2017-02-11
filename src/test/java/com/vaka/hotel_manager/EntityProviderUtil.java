package com.vaka.hotel_manager;

import com.vaka.hotel_manager.core.context.ApplicationContextHolder;
import com.vaka.hotel_manager.domain.ReservationStatus;
import com.vaka.hotel_manager.domain.Role;
import com.vaka.hotel_manager.domain.dto.ReservationDTO;
import com.vaka.hotel_manager.domain.dto.RoomClassDTO;
import com.vaka.hotel_manager.domain.entity.*;
import com.vaka.hotel_manager.repository.*;
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
    private static ReservationRepository reservationRepository = ApplicationContextHolder.getContext().getBean(ReservationRepository.class);
    private static UserRepository userRepository = ApplicationContextHolder.getContext().getBean(UserRepository.class);
    private static RoomRepository roomRepository = ApplicationContextHolder.getContext().getBean(RoomRepository.class);
    private static BillRepository billRepository = ApplicationContextHolder.getContext().getBean(BillRepository.class);
    private static RoomClassRepository roomClassRepository = ApplicationContextHolder.getContext().getBean(RoomClassRepository.class);
    private static Random random = new Random();

    private static int roomNumber = 1;
    private static int emailNumber = 1;
    private static int vkId = 1;

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
        room.setCapacity(4);
        room.setCostPerDay(200);
        room.setNumber(roomNumber++);
        room.setRoomClass(roomClass);
        return room;
    }


    public static Reservation createReservationWithoutRoom(RoomClass requestedRoomClass, User user) {
        Reservation reservation = new Reservation();
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
        user.setName("name");
        user.setEmail("goodmanmen@gmail.com" + emailNumber++);
        user.setPhoneNumber("+398641423");
        user.setRole(Role.CUSTOMER);
        user.setVkId(vkId++);

        user.setPassword(BCrypt.hashpw(String.join(":", user.getName(), user.getPhoneNumber()), BCrypt.gensalt()));
        return user;
    }

    public static Bill createBill(Reservation reservation) {
        Bill bill = new Bill();
        bill.setReservation(reservation);
        bill.setTotalCost((int) (reservation.getRoom().getCostPerDay() * (reservation.getDepartureDate().toEpochDay() - reservation.getArrivalDate().toEpochDay())));
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
