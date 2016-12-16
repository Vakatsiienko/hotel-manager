package com.vaka.hotel_manager.repository.inMemoryImpl;

import com.vaka.hotel_manager.context.ApplicationContext;
import com.vaka.hotel_manager.domain.Room;
import com.vaka.hotel_manager.domain.RoomClass;
import com.vaka.hotel_manager.repository.ReservationRepository;
import com.vaka.hotel_manager.repository.RoomRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Iaroslav on 11/26/2016.
 */
@Deprecated
public class RoomRepositoryInMemoryImpl implements RoomRepository {
    private ReservationRepository reservationRepository;
    private Map<Integer, Room> roomById = new ConcurrentHashMap<>();
    private AtomicInteger idCounter = ApplicationContext.getInstance().getIdCounter();


    @Override
    public List<Room> findAvailableForReservation(RoomClass roomClass, LocalDate arrivalDate, LocalDate departureDate) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Room> findAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Room create(Room entity) {
        entity.setId(idCounter.getAndIncrement());
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
