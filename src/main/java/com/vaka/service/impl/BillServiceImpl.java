package com.vaka.service.impl;

import com.vaka.domain.Bill;
import com.vaka.domain.Reservation;
import com.vaka.repository.BillRepository;
import com.vaka.service.BillService;
import com.vaka.util.ApplicationContext;
import com.vaka.util.ServletDomainExtractor;

/**
 * Created by Iaroslav on 11/26/2016.
 */
public class BillServiceImpl implements BillService {

    /**
     * Don't invoke variable directly, use {@link #getBillRepository()}
     */
    private BillRepository billRepository;

    @Override
    public Bill createFromReservation(Reservation reservation) {
            return create(ServletDomainExtractor.createFromReservation(reservation));
    }

    @Override
    public Bill create(Bill entity) {
            return getBillRepository().create(entity);

    }

    @Override
    public Bill getById(Integer id) {
            return getBillRepository().getById(id);

    }

    @Override
    public boolean delete(Integer id) {
            return getBillRepository().delete(id);

    }

    @Override
    public Bill update( Integer id, Bill entity) {
            return getBillRepository().update(id, entity);

    }

    public BillRepository getBillRepository() {
        if (billRepository == null) {
            synchronized (this) {
                if (billRepository == null) {
                    billRepository = ApplicationContext.getBean(BillRepository.class);
                }
            }
        }
        return billRepository;
    }
}
