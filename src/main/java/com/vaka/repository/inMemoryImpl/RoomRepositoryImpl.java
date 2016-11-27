package com.vaka.repository.inMemoryImpl;

import com.vaka.domain.BathroomType;
import com.vaka.domain.ReservationRequest;
import com.vaka.domain.Room;
import com.vaka.repository.RoomRepository;
import com.vaka.util.ApplicationContext;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Iaroslav on 11/26/2016.
 */
public class RoomRepositoryImpl implements RoomRepository {
    private Map<Integer, Room> roomById = new ConcurrentHashMap<>();
    private AtomicInteger idCounter = ApplicationContext.getIdCounter();

    @Override
    public List<Room> findForRequest(ReservationRequest reservationRequest) {
        Stream<Room> rooms = roomById.values().stream()
                .filter(r -> r.getClazz() == reservationRequest.getRoomClass() &&
                        r.getNumOfBeds() >= reservationRequest.getNumOfBeds() &&
                        (r.getCostPerDay() * reservationRequest.getPeriod().getDays()) <= reservationRequest.getTotalCost());
        if (reservationRequest.getBathroomType() != BathroomType.ANY) {
            rooms.filter(r -> r.getBathroomType() == reservationRequest.getBathroomType());
        }

        return rooms.collect(Collectors.toList());
    }

    @Override
    public Room persist(Room entity) {
        entity.setId(idCounter.getAndIncrement());
        roomById.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Room getById(Integer id) {
        return roomById.get(id);
    }

    @Override
    public boolean delete(Integer id) {
        return roomById.remove(id) != null;
    }

    @Override
    public Room update(Integer id, Room entity) {
        entity.setId(id);
        roomById.put(id, entity);
        return entity;
    }
}
