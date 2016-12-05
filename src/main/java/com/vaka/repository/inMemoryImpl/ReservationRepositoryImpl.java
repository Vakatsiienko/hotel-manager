package com.vaka.repository.inMemoryImpl;

import com.vaka.domain.*;
import com.vaka.repository.UserRepository;
import com.vaka.repository.ReservationRepository;
import com.vaka.context.ApplicationContext;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Created by Iaroslav on 11/27/2016.
 */
public class ReservationRepositoryImpl implements ReservationRepository {
    private Map<Integer, Reservation> reservationById = new ConcurrentHashMap<>();
    private AtomicInteger idCounter = ApplicationContext.getInstance().getIdCounter();
    private UserRepository userRepository;

//    {
//        Random random = new Random();
//        for (int i = 0; i < 10; i++) {
//            Reservation reservation = new Reservation();
//            reservation.setRequestedRoomClass(RoomClass.values()[random.nextInt(3)]);
//            reservation.setId(idCounter.getAndIncrement());
//            reservation.setGuests(random.nextInt(10));
//            reservation.setStatus(ReservationStatus.REQUESTED);
//            reservation.setUser(new User(i + " someMail", "password", i + "someName", Role.CUSTOMER, "+3098"));//getCustomerRep.create
//            int arrivalMonth = random.nextInt(12) + 1;
//            int arrivalDay = random.nextInt(25) + 1;
//            reservation.setArrivalDate(LocalDate.of(2017, arrivalMonth, arrivalDay));
//            reservation.setDepartureDate(LocalDate.of(2017, random.nextInt(13 - arrivalMonth) + arrivalMonth,
//                    random.nextInt(26 - arrivalDay) + arrivalDay));
//            reservation.setCreatedDatetime(LocalDateTime.now());
//            reservationById.put(reservation.getId(), reservation);
//        }
//        Reservation reservation1 = new Reservation();
//        reservation1.setRequestedRoomClass(RoomClass.FIRST_CLASS);
//        reservation1.setId(idCounter.getAndIncrement());
//        reservation1.setGuests(random.nextInt(10));
//        reservation1.setStatus(ReservationStatus.REQUESTED);
//        reservation1.setUser(new User("firstMail", "password", "FirstName", Role.CUSTOMER, "+3098"));//getCustomerRep.create
//        int arrivalMonth = random.nextInt(12) + 1;
//        int arrivalDay = random.nextInt(25) + 1;
//        int departureMonth;
//        int departureDay;
//        reservation1.setArrivalDate(LocalDate.of(2017, arrivalMonth, arrivalDay));
//        reservation1.setDepartureDate(LocalDate.of(2017, departureMonth = random.nextInt(13 - arrivalMonth) + arrivalMonth,
//                departureDay = random.nextInt(26 - arrivalDay) + arrivalDay));
//        reservation1.setCreatedDatetime(LocalDateTime.now());
//        reservationById.put(reservation1.getId(), reservation1);
//        Reservation reservation2 = new Reservation();
//        reservation2.setRequestedRoomClass(RoomClass.FIRST_CLASS);
//        reservation2.setId(idCounter.getAndIncrement());
//        reservation2.setGuests(random.nextInt(10));
//        reservation2.setStatus(ReservationStatus.REQUESTED);
//        reservation2.setUser(new User("secondMail", "password", "SecondName", Role.CUSTOMER, "+3098"));//getCustomerRep.create
//        arrivalMonth = random.nextInt(12) + 1;
//        arrivalDay = random.nextInt(25) + 1;
//        reservation2.setArrivalDate(LocalDate.of(2017, arrivalMonth, arrivalDay));
//        reservation2.setDepartureDate(LocalDate.of(2017, departureMonth, departureDay));
//        reservation2.setCreatedDatetime(LocalDateTime.now());
//        reservationById.put(reservation2.getId(), reservation2);
//
//    }

    @Override
    public Reservation create(Reservation entity) {
        entity.setId(idCounter.getAndIncrement());
        entity.setCreatedDatetime(LocalDateTime.now());
        reservationById.put(entity.getId(), entity);
        return entity;
    }
//
//    @Override
//    public List<Reservation> findRequested() {
//        return reservationById.values().stream().filter(r -> r.getStatus() == ReservationStatus.REQUESTED).collect(Collectors.toList());
//    }
//
//    @Override
//    public List<Reservation> findByStatus() {
//        return reservationById.values().stream().filter(r -> r.getStatus() == ReservationStatus.CONFIRMED).collect(Collectors.toList());
//    }
//
//    @Override
//    public List<Reservation> findRejected() {
//        return reservationById.values().stream().filter(r -> r.getStatus() == ReservationStatus.REJECTED).collect(Collectors.toList());
//    }

    @Override
    public List<Reservation> findByStatus(ReservationStatus status) {
        return reservationById.values().stream().filter(r -> r.getStatus() == status).collect(Collectors.toList());
    }
    @Override
    public List<Reservation> findByRoomIdAndStatus(Integer roomId, ReservationStatus status) {
        return reservationById.values().stream()
                .filter(r -> r.getStatus() == ReservationStatus.CONFIRMED && r.getRoom().getId().equals(roomId))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Reservation> getById(Integer id) {
        return Optional.of(reservationById.get(id));
    }

    @Override
    public boolean delete(Integer id) {
        return reservationById.remove(id) != null;
    }

    @Override
    public boolean update(Integer id, Reservation entity) {
        entity.setId(id);
        if (reservationById.containsKey(id)) {
            reservationById.put(id, entity);
            return true;
        } else return false;
    }

    public UserRepository getUserRepository() {
        if (userRepository == null) {
            synchronized (this) {
                if (userRepository == null) {
                    userRepository = ApplicationContext.getInstance().getBean(UserRepository.class);
                }
            }
        }
        return userRepository;
    }
}
