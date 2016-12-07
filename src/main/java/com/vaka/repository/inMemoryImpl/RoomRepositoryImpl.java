package com.vaka.repository.inMemoryImpl;

import com.vaka.domain.ReservationStatus;
import com.vaka.domain.Room;
import com.vaka.domain.RoomClass;
import com.vaka.repository.ReservationRepository;
import com.vaka.repository.RoomRepository;
import com.vaka.context.ApplicationContext;
import com.vaka.util.DateAndTimeUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Iaroslav on 11/26/2016.
 */
public class RoomRepositoryImpl implements RoomRepository {
    private ReservationRepository reservationRepository;
    private Map<Integer, Room> roomById = new ConcurrentHashMap<>();
    private AtomicInteger idCounter = ApplicationContext.getInstance().getIdCounter();


//    {
//        Random random = new Random();
//        for (int i = 0; i < 100; i++) {
//            Room room = new Room(random.nextInt(1000), random.nextInt(10), 150, RoomClass.values()[random.nextInt(3)], "");
//            room.setId(idCounter.getAndIncrement());
//            roomById.put(room.getId(), room);
//        }
//    }

    @Override
    public List<Room> findAvailableForReservation(RoomClass roomClass, LocalDate arrivalDate, LocalDate departureDate) {
        Stream<Room> rooms = roomById.values().stream()
                .filter(r -> r.getRoomClazz() == roomClass &&
                        !getReservationRepository().findByRoomIdAndStatus(r.getId(), ReservationStatus.CONFIRMED).stream()
                                .filter(reservation -> !DateAndTimeUtil.areDatesOverlap(
                                        reservation.getArrivalDate(), reservation.getDepartureDate(),
                                        arrivalDate, departureDate)
                                ).findFirst().isPresent());

        List<Room> roomsList = rooms.collect(Collectors.toList());
        System.out.println("Rooms quantity:" + roomsList.size());
        return roomsList;
    }

    @Override
    public Room create(Room entity) {
        entity.setId(idCounter.getAndIncrement());
        entity.setCreatedDatetime(LocalDateTime.now());
        roomById.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<Room> getById(Integer id) {
        return Optional.of(roomById.get(id));
    }

    @Override
    public boolean delete(Integer id) {
        return roomById.remove(id) != null;
    }

    @Override
    public boolean update(Integer id, Room entity) {
        entity.setId(id);
        if (roomById.containsKey(id)) {
            roomById.put(id, entity);
            return true;
        } else return false;
    }

    public ReservationRepository getReservationRepository() {
        if (reservationRepository == null) {
            synchronized (this) {
                if (reservationRepository == null) {
                    reservationRepository = ApplicationContext.getInstance().getBean(ReservationRepository.class);
                }
            }
        }
        return reservationRepository;
    }
}
