package com.vaka.repository.inMemoryImpl;

import com.vaka.domain.ReservationRequest;
import com.vaka.domain.ReservationRequestStatus;
import com.vaka.repository.ReservationRequestRepository;
import com.vaka.util.ApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Created by Iaroslav on 11/26/2016.
 */
public class ReservationRequestRepositoryImpl implements ReservationRequestRepository {
    private Map<Integer, ReservationRequest> reservationRequestById = new HashMap<>();
    private AtomicInteger idCounter = ApplicationContext.getIdCounter();

    @Override
    public List<ReservationRequest> list() {
        return reservationRequestById.values().stream()
                .filter(r -> r.getStatus() == ReservationRequestStatus.WAITING)
                .collect(Collectors.toList());
    }

    @Override
    public ReservationRequest persist(ReservationRequest entity) {
        entity.setId(idCounter.getAndIncrement());
        reservationRequestById.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public ReservationRequest getById(Integer id) {
        return reservationRequestById.get(id);
    }

    @Override
    public boolean delete(Integer id) {
        return reservationRequestById.remove(id) != null;
    }

    @Override
    public ReservationRequest update(Integer id, ReservationRequest entity) {
        entity.setId(id);
        reservationRequestById.put(id, entity);
        return entity;
    }
}
