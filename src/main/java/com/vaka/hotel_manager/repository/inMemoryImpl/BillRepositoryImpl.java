package com.vaka.hotel_manager.repository.inMemoryImpl;

import com.vaka.hotel_manager.context.ApplicationContext;
import com.vaka.hotel_manager.domain.Bill;
import com.vaka.hotel_manager.domain.Reservation;
import com.vaka.hotel_manager.repository.BillRepository;
import com.vaka.hotel_manager.repository.ReservationRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created by Iaroslav on 11/26/2016.
 */
public class BillRepositoryImpl implements BillRepository {
    private Map<Integer, Bill> billById = new ConcurrentHashMap<>();
    private AtomicInteger idCounter = ApplicationContext.getInstance().getIdCounter();
    private ReservationRepository reservationRepository;


    @Override
    public Bill create(Bill entity) {
        entity.setId(idCounter.getAndIncrement());
        billById.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<Bill> getByReservationId(Integer id) {
        Optional<Reservation> reservation = getReservationRepository().getById(id);
        if (!reservation.isPresent())
            return null;
        return billById.values().stream().filter(b -> b.getReservation().getId().equals(id)).findFirst();
    }

    @Override
    public Optional<Bill> getById(Integer id) {
        return Optional.of(billById.get(id));
    }

    @Override
    public boolean delete(Integer id) {
        return billById.remove(id) != null;
    }

    @Override
    public boolean update(Integer id, Bill entity) {
        entity.setId(id);
        if (billById.containsKey(id)) {
            billById.put(id, entity);
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
