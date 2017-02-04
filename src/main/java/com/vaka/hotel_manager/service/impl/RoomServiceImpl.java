package com.vaka.hotel_manager.service.impl;

import com.vaka.hotel_manager.core.context.ApplicationContextHolder;
import com.vaka.hotel_manager.core.security.SecurityService;
import com.vaka.hotel_manager.core.security.SecurityUtils;
import com.vaka.hotel_manager.core.tx.TransactionManager;
import com.vaka.hotel_manager.domain.Page;
import com.vaka.hotel_manager.domain.entity.Reservation;
import com.vaka.hotel_manager.domain.entity.Room;
import com.vaka.hotel_manager.domain.entity.RoomClass;
import com.vaka.hotel_manager.domain.entity.User;
import com.vaka.hotel_manager.repository.ReservationRepository;
import com.vaka.hotel_manager.repository.RoomClassRepository;
import com.vaka.hotel_manager.repository.RoomRepository;
import com.vaka.hotel_manager.service.RoomService;
import com.vaka.hotel_manager.util.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

/**
 * Created by Iaroslav on 11/26/2016.
 */
public class RoomServiceImpl implements RoomService {
    private RoomRepository roomRepository;
    private ReservationRepository reservationRepository;
    private SecurityService securityService;
    private RoomClassRepository roomClassRepository;
    private TransactionManager transactionManager;
    private static final Logger LOG = LoggerFactory.getLogger(RoomServiceImpl.class);

    @Override
    public List<Room> findAvailableByClassAndDates(User loggedUser, RoomClass roomClass,
                                                   LocalDate arrivalDate, LocalDate departureDate) {
        getSecurityService().authorize(loggedUser, SecurityUtils.ANONYMOUS_ACCESS_ROLES);
        LOG.debug("Finding available rooms by RoomClass and dates");
        return getRoomRepository().findAvailableForReservation(roomClass, arrivalDate, departureDate);
    }

    @Override
    public List<Room> findAvailableForReservation(User loggedUser, Integer reservationId) {
        getSecurityService().authorize(loggedUser, SecurityUtils.CUSTOMER_ACCESS_ROLES);
        LOG.debug("Finding available rooms for reservation by reservationId: {}", reservationId);
            Optional<Reservation> request = getReservationRepository().getById(reservationId);
            if (request.isPresent()) {
                return getRoomRepository().findAvailableForReservation(request.get().getRequestedRoomClass(),
                        request.get().getArrivalDate(), request.get().getDepartureDate());
            } else throw new NotFoundException("Reservation Not Found");
    }

    @Override
    public List<Room> findAll(User loggedUser) {
        LOG.debug("Finding all rooms");
        return getRoomRepository().findAll();
    }

    @Override
    public Room create(User loggedUser, Room room) {
        getSecurityService().authorize(loggedUser, SecurityUtils.MANAGER_ACCESS_ROLES);
        room.setCreatedDatetime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        LOG.debug("Creating room: {}", room);
        return getTransactionManager().doTransactional(TransactionManager.TRANSACTION_REPEATABLE_READ, () -> { //TODO add constraint exception handle
            Optional<RoomClass> rc = getRoomClassRepository().getByName(room.getRoomClass().getName());
            if (!rc.isPresent())
                throw new IllegalArgumentException("Such room class doesn't exist");
            room.setRoomClass(rc.get());
            if (!getRoomRepository().getByNumber(room.getNumber()).isPresent()) {
                return getRoomRepository().create(room);
            } else throw new IllegalArgumentException("Room with such number already exist");
        });
    }

    @Override
    public Optional<Room> getById(User loggedUser, Integer id) {
        LOG.debug("Getting room by id: {}", id);
        return getRoomRepository().getById(id);
    }

    @Override
    public boolean delete(User loggedUser, Integer id) {
        getSecurityService().authorize(loggedUser, SecurityUtils.MANAGER_ACCESS_ROLES);
        LOG.debug("Deleting room by id: {}", id);
        return getRoomRepository().delete(id);
    }

    @Override
    public boolean update(User loggedUser, Integer id, Room room) {
        getSecurityService().authorize(loggedUser, SecurityUtils.MANAGER_ACCESS_ROLES);
        LOG.debug("Updating room with id: {}, state: {}", id, room);
        return getTransactionManager().doTransactional(TransactionManager.TRANSACTION_REPEATABLE_READ, () -> {
            Optional<Room> old = getRoomRepository().getById(id);
            validateRoomForUpdate(room, old);
            return getRoomRepository().update(id, room);
        });
    }

    private void validateRoomForUpdate(Room newRoom, Optional<Room> oldRoom) { //TODO replace validation with constraint exception handling
        if (!oldRoom.isPresent())
            throw new IllegalArgumentException("Room with such id doesn't exist");
        else {
            if (!oldRoom.get().getNumber().equals(newRoom.getNumber())) {
                if (getRoomRepository().getByNumber(newRoom.getNumber()).isPresent()) {
                    throw new IllegalArgumentException("Room with such number already exist");
                }
            }
            if (!oldRoom.get().getRoomClass().getName().equals(newRoom.getRoomClass().getName())) {
                Optional<RoomClass> rc = getRoomClassRepository().getByName(newRoom.getRoomClass().getName());
                if (!rc.isPresent()) {
                    throw new IllegalArgumentException("Such room class doesn't exist");
                }
                newRoom.setRoomClass(rc.get()); //TODO consider where to move setRoomClass
            } else newRoom.setRoomClass(oldRoom.get().getRoomClass());
        }
    }

    @Override
    public Page<Room> findPage(User loggedUser, Integer page, Integer rows) {
        LOG.debug(String.format("Finding room page, page: %s, rows count: %s", page, rows));
        return getRoomRepository().findPage(page, rows);
    }

    public RoomRepository getRoomRepository() {
        if (roomRepository == null) {
            roomRepository = ApplicationContextHolder.getContext().getBean(RoomRepository.class);
        }
        return roomRepository;
    }

    public ReservationRepository getReservationRepository() {
        if (reservationRepository == null) {
            reservationRepository = ApplicationContextHolder.getContext().getBean(ReservationRepository.class);
        }
        return reservationRepository;
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

    public RoomClassRepository getRoomClassRepository() {
        if (roomClassRepository == null) {
            roomClassRepository = ApplicationContextHolder.getContext().getBean(RoomClassRepository.class);
        }
        return roomClassRepository;
    }
}
