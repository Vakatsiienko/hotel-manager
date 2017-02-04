package com.vaka.hotel_manager.service.impl;

import com.vaka.hotel_manager.core.context.ApplicationContextHolder;
import com.vaka.hotel_manager.core.security.SecurityService;
import com.vaka.hotel_manager.core.security.SecurityUtils;
import com.vaka.hotel_manager.core.tx.TransactionManager;
import com.vaka.hotel_manager.domain.entity.RoomClass;
import com.vaka.hotel_manager.domain.entity.User;
import com.vaka.hotel_manager.repository.RoomClassRepository;
import com.vaka.hotel_manager.repository.RoomRepository;
import com.vaka.hotel_manager.service.RoomClassService;
import com.vaka.hotel_manager.util.exception.CreatingException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

/**
 * Created by Iaroslav on 1/22/2017.
 */
public class RoomClassServiceImpl implements RoomClassService {
    private RoomClassRepository roomClassRepository;
    private SecurityService securityService;
    private TransactionManager transactionManager;
    private RoomRepository roomRepository;

    @Override
    public List<RoomClass> findAll(User loggedUser) {
        return getRoomClassRepository().findAll();
    }

    @Override
    public RoomClass create(User loggedUser, RoomClass entity) {
        getSecurityService().authorize(loggedUser, SecurityUtils.MANAGER_ACCESS_ROLES);
        entity.setCreatedDatetime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        return getTransactionManager().doTransactional(TransactionManager.TRANSACTION_SERIALIZABLE, () -> {
            Optional<RoomClass> roomClass = getRoomClassRepository().getByName(entity.getName());
            if (roomClass.isPresent())//TODO add constraint exception handle
                throw new CreatingException("Room Class with such name already exist");
            return getRoomClassRepository().create(entity);
        });
    }

    @Override
    public Optional<RoomClass> getById(User loggedUser, Integer id) {
        return getRoomClassRepository().getById(id);
    }

    @Override
    public boolean delete(User loggedUser, Integer id) {
        getSecurityService().authorize(loggedUser, SecurityUtils.MANAGER_ACCESS_ROLES);
        return getTransactionManager().doTransactional(TransactionManager.TRANSACTION_SERIALIZABLE, () ->//TODO add constraint exception handle
                !getRoomRepository().existsRoomByRoomClass(id) && getRoomClassRepository().delete(id));
    }

    @Override
    public boolean update(User loggedUser, Integer id, RoomClass entity) {
        getSecurityService().authorize(loggedUser, SecurityUtils.MANAGER_ACCESS_ROLES);
        return getRoomClassRepository().update(id, entity);
    }

    public RoomClassRepository getRoomClassRepository() {
        if (roomClassRepository == null) {
            roomClassRepository = ApplicationContextHolder.getContext().getBean(RoomClassRepository.class);
        }
        return roomClassRepository;
    }

    public SecurityService getSecurityService() {
        if (securityService == null) {
            securityService = ApplicationContextHolder.getContext().getBean(SecurityService.class);
        }
        return securityService;
    }

    public TransactionManager getTransactionManager() {
        if (transactionManager == null) {
            transactionManager = ApplicationContextHolder.getContext().getBean(TransactionManager.class);
        }
        return transactionManager;
    }

    public RoomRepository getRoomRepository() {
        if (roomRepository == null) {
            roomRepository = ApplicationContextHolder.getContext().getBean(RoomRepository.class);
        }
        return roomRepository;
    }
}
