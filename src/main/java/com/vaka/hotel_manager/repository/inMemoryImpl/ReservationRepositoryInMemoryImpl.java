package com.vaka.hotel_manager.repository.inMemoryImpl;

import com.vaka.hotel_manager.context.ApplicationContext;
import com.vaka.hotel_manager.domain.DTO.ReservationDTO;
import com.vaka.hotel_manager.domain.Reservation;
import com.vaka.hotel_manager.domain.ReservationStatus;
import com.vaka.hotel_manager.repository.ReservationRepository;
import com.vaka.hotel_manager.repository.UserRepository;
import com.vaka.hotel_manager.util.exception.RepositoryException;
import com.vaka.hotel_manager.util.repository.NamedPreparedStatement;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Created by Iaroslav on 11/27/2016.
 */
@Deprecated
public class ReservationRepositoryInMemoryImpl implements ReservationRepository {
    private Map<Integer, Reservation> reservationById = new ConcurrentHashMap<>();
    private AtomicInteger idCounter = ApplicationContext.getInstance().getIdCounter();
    private UserRepository userRepository;

    @Override
    public Reservation create(Reservation entity) {
        entity.setId(idCounter.getAndIncrement());
        reservationById.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public boolean existOverlapReservation(Integer roomId, LocalDate arrivalDate, LocalDate departureDate) {
        return reservationById.values().stream().filter(reservation -> reservation.getRoom().getId().equals(roomId) &&
                reservation.getStatus() == ReservationStatus.CONFIRMED &&
                reservation.getArrivalDate().isBefore(departureDate) &&
                reservation.getDepartureDate().isBefore(arrivalDate)).count() != 0;
    }

//    @Override
//    public List<ReservationDTO> findByRoomIdAndStatus(Integer roomId, ReservationStatus status) {
//        String strCountRowsQuery = getQueryByClassAndMethodName().get("reservation.findByRoomIdAndStatus_count");
//        String strQuery = getQueryByClassAndMethodName().get("reservation.findByRoomIdAndStatus");
//        try (Connection connection = getDataSource().getConnection();
//             NamedPreparedStatement statement = getFindByRoomIdAndStatusStatement(connection, strQuery, roomId, status);
//             ResultSet resultSet = statement.executeQuery();
//             NamedPreparedStatement countStatement = getFindByRoomIdAndStatusStatement(connection, strCountRowsQuery, roomId, status);
//             ResultSet countSet = countStatement.executeQuery()) {
//
//            return fetchDTOList(resultSet, countSet);
//        } catch (SQLException e) {
//            LOG.info(e.getMessage());
//            throw new RepositoryException(e);
//        }
//    }
//
//    private NamedPreparedStatement getFindByRoomIdAndStatusStatement(Connection connection, String query, Integer roomId, ReservationStatus status) throws SQLException {
//        NamedPreparedStatement statement = new NamedPreparedStatement(connection, query).init();
//        statement.setStatement("roomId", roomId);
//        statement.setStatement("status", status.name());
//        return statement;
//    }

    @Override
    public List<ReservationDTO> findByUserIdAndStatus(Integer userId, ReservationStatus status) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<ReservationDTO> findByStatusFromDate(ReservationStatus status, LocalDate fromDate) {
        throw new UnsupportedOperationException();
    }

//    @Override
//    public List<ReservationDTO> findByRoomIdAndStatus(Integer roomId, ReservationStatus status) {
//        throw new UnsupportedOperationException();
//    }

    @Override
    public List<ReservationDTO> findActiveByUserId(Integer userId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Reservation> getById(Integer id) {
        return Optional.of(reservationById.get(id));
    }

    @Override
    public boolean delete(Integer id) {
        return reservationById.remove(id) != null;
    }

    @Override
    public boolean update(Integer id, Reservation entity) {
        entity.setId(id);
        if (reservationById.containsKey(id)) {
            reservationById.put(id, entity);
            return true;
        } else return false;
    }

    public UserRepository getUserRepository() {
        if (userRepository == null) {
            synchronized (this) {
                if (userRepository == null) {
                    userRepository = ApplicationContext.getInstance().getBean(UserRepository.class);
                }
            }
        }
        return userRepository;
    }
}
