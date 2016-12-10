package com.vaka.hotel_manager.repository.inMemoryImpl;

import com.vaka.hotel_manager.context.ApplicationContext;
import com.vaka.hotel_manager.domain.Reservation;
import com.vaka.hotel_manager.domain.ReservationStatus;
import com.vaka.hotel_manager.repository.ReservationRepository;
import com.vaka.hotel_manager.repository.UserRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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

    @Override
    public Reservation create(Reservation entity) {
        entity.setId(idCounter.getAndIncrement());
        reservationById.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public boolean existOverlapReservation(Integer roomId, LocalDate arrivalDate, LocalDate departureDate) {
        return reservationById.values().stream().filter(reservation -> reservation.getRoom().getId().equals(roomId) &&
                reservation.getStatus() == ReservationStatus.CONFIRMED &&
                reservation.getArrivalDate().isBefore(departureDate) &&
                reservation.getDepartureDate().isBefore(arrivalDate)).count() != 0;
    }

    @Override
    public List<Reservation> findByUserIdAndStatus(Integer userId, ReservationStatus status) {
        return reservationById.values().stream()
                .filter(r -> r.getUser().getId().equals(userId) && r.getStatus() == status)
                .collect(Collectors.toList());
    }

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
    public List<Reservation> findActiveByUserId(Integer userId) {
        return reservationById.values().stream()
                .filter(reservation -> reservation.getUser().getId().equals(userId) &&
                        reservation.getDepartureDate().isAfter(LocalDate.now()))
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
