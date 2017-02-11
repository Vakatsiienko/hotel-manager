package com.vaka.hotel_manager.service.impl;

import com.vaka.hotel_manager.core.context.ApplicationContextHolder;
import com.vaka.hotel_manager.core.security.SecurityService;
import com.vaka.hotel_manager.core.security.SecurityUtils;
import com.vaka.hotel_manager.domain.entity.RoomClass;
import com.vaka.hotel_manager.domain.entity.User;
import com.vaka.hotel_manager.repository.RoomClassRepository;
import com.vaka.hotel_manager.repository.exception.ConstraintViolationException;
import com.vaka.hotel_manager.repository.exception.ConstraintViolationType;
import com.vaka.hotel_manager.service.RoomClassService;
import com.vaka.hotel_manager.util.exception.CreatingException;

import java.util.List;
import java.util.Optional;

/**
 * Created by Iaroslav on 1/22/2017.
 */
public class RoomClassServiceImpl implements RoomClassService {
    private RoomClassRepository roomClassRepository;
    private SecurityService securityService;

    @Override
    public List<RoomClass> findAll(User loggedUser) {
        return getRoomClassRepository().findAll();
    }

    @Override
    public RoomClass create(User loggedUser, RoomClass entity) {
        getSecurityService().authorize(loggedUser, SecurityUtils.MANAGER_ACCESS_ROLES);
        try {
            return getRoomClassRepository().create(entity);
        } catch (ConstraintViolationException e) {
            if (e.getViolationType() == ConstraintViolationType.DUPLICATE_ENTRY && e.getViolatedField().equals("name")) {
                throw new CreatingException("Room Class with such name already exist");
            } else throw e;
        }
    }

    @Override
    public Optional<RoomClass> getById(User loggedUser, Integer id) {
        return getRoomClassRepository().getById(id);
    }

    @Override
    public boolean delete(User loggedUser, Integer id) {
        getSecurityService().authorize(loggedUser, SecurityUtils.MANAGER_ACCESS_ROLES);
        try {
            return getRoomClassRepository().delete(id);
        } catch (ConstraintViolationException e){
            if (e.getViolationType() == ConstraintViolationType.FOREIGN_KEY_DELETE_OR_UPDATE && e.getViolatedField().equals("roomClassId")) {
                throw new IllegalArgumentException("You can't delete room class while rooms with this room class exist");//TODO create handling strategy
            }
            throw e;
        }
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


}
