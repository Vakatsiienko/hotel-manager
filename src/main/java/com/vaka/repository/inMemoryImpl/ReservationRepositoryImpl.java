package com.vaka.repository.inMemoryImpl;

import com.vaka.domain.Reservation;
import com.vaka.repository.ReservationRepository;
import com.vaka.util.ApplicationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Iaroslav on 11/27/2016.
 */
public class ReservationRepositoryImpl implements ReservationRepository {
    private Map<Integer, Reservation> reservationById = new ConcurrentHashMap<>();
    private AtomicInteger idCounter = ApplicationContext.getIdCounter();

    @Override
    public Reservation persist(Reservation entity) {
        entity.setId(idCounter.getAndIncrement());
        reservationById.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Reservation getById(Integer id) {
        return reservationById.get(id);
    }

    @Override
    public boolean delete(Integer id) {
        return reservationById.remove(id) != null;
    }

    @Override
    public Reservation update(Integer id, Reservation entity) {
        entity.setId(id);
        reservationById.put(id, entity);
        return entity;
    }
}
