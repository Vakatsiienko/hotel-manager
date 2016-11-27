package com.vaka.repository.inMemoryImpl;

import com.vaka.domain.Bill;
import com.vaka.repository.BillRepository;
import com.vaka.util.ApplicationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created by Iaroslav on 11/26/2016.
 */
public class BillRepositoryImpl implements BillRepository {
    private Map<Integer, Bill> billById = new ConcurrentHashMap<>();
    private AtomicInteger idCounter = ApplicationContext.getIdCounter();

    @Override
    public Bill persist(Bill entity) {
        entity.setId(idCounter.getAndIncrement());
        billById.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Bill getById(Integer id) {
        return billById.get(id);
    }

    @Override
    public boolean delete(Integer id) {
        return billById.remove(id) != null;
    }

    @Override
    public Bill update(Integer id, Bill entity) {
        entity.setId(id);
        billById.put(id, entity);
        return entity;
    }
}
