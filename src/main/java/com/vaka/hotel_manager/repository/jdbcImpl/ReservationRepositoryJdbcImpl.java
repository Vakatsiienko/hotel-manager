package com.vaka.hotel_manager.repository.jdbcImpl;

import com.vaka.hotel_manager.core.context.ApplicationContextHolder;
import com.vaka.hotel_manager.core.tx.ConnectionManager;
import com.vaka.hotel_manager.domain.Page;
import com.vaka.hotel_manager.domain.ReservationStatus;
import com.vaka.hotel_manager.domain.dto.ReservationDTO;
import com.vaka.hotel_manager.domain.entity.Reservation;
import com.vaka.hotel_manager.repository.ReservationRepository;
import com.vaka.hotel_manager.repository.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Iaroslav on 12/4/2016.
 */
public class ReservationRepositoryJdbcImpl implements ReservationRepository {
    private static final Logger LOG = LoggerFactory.getLogger(ReservationRepositoryJdbcImpl.class);
    private ConnectionManager connectionManager;
    private JdbcCrudHelper crudHelper;
    private Map<String, String> queryByClassAndMethodName;

    @Override
    public boolean existOverlapReservations(Integer roomId, LocalDate arrivalDate, LocalDate departureDate) {
        String strQuery = getQueryByClassAndMethodName().get("reservation.existOverlapReservation");
        RepositoryUtils.logQuery(LOG, strQuery, roomId, arrivalDate, departureDate);
        return getConnectionManager().withConnection(connection -> {
            try (NamedPreparedStatement statement = getExistOverlapReservationStatement(connection, strQuery, roomId, arrivalDate, departureDate);
                 ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getInt(1) != 0;
            }
        });
    }

    private NamedPreparedStatement getExistOverlapReservationStatement(Connection connection, String query, Integer roomId, LocalDate arrivalDate, LocalDate departureDate) throws SQLException {
        NamedPreparedStatement statement = NamedPreparedStatement.create(connection, query);
        statement.setStatement("roomId", roomId);
        statement.setStatement("arrivalDate", Date.valueOf(arrivalDate));
        statement.setStatement("departureDate", Date.valueOf(departureDate));
        return statement;
    }


    private List<ReservationDTO> fetchDTOList(ResultSet resultSet) throws SQLException {
        List<ReservationDTO> reservations = new ArrayList<>(resultSet.getMetaData().getColumnCount());
        while (resultSet.next()) {
            reservations.add(StatementToDomainExtractor.extractReservationDTO(resultSet));
        }
        return reservations;
    }

    @Override
    public List<ReservationDTO> findByUserIdAndStatus(Integer userId, ReservationStatus status) {
        String strQuery = getQueryByClassAndMethodName().get("reservation.findByUserIdAndStatus");
        RepositoryUtils.logQuery(LOG, strQuery, userId, status);
        return getConnectionManager().withConnection(connection -> {
            try (NamedPreparedStatement statement = getFindByUserIdAndStatusStatement(connection, strQuery, userId, status);
                 ResultSet resultSet = statement.executeQuery()) {
                return fetchDTOList(resultSet);
            }
        });
    }

    private NamedPreparedStatement getFindByUserIdAndStatusStatement(Connection connection, String query, Integer userId, ReservationStatus status) throws SQLException {
        NamedPreparedStatement statement = NamedPreparedStatement.create(connection, query);
        statement.setStatement("userId", userId);
        statement.setStatement("status", status.name());
        return statement;
    }

    @Override
    public Page<ReservationDTO> findPageByStatusFromDate(ReservationStatus status, LocalDate fromDate, Integer page, Integer size) {
        String strQuery = getQueryByClassAndMethodName().get("reservation.findPageByStatusFromDate");
        String strCountQuery = getQueryByClassAndMethodName().get("reservation.findPageByStatusFromDateCount");
        RepositoryUtils.logQuery(LOG, strQuery, status, fromDate);
        return getConnectionManager().withConnection(connection -> {
            try (NamedPreparedStatement statement = getPageByStatusStatementFromDate(connection, strQuery, status, fromDate, (page - 1) * size, size);
                 ResultSet resultSet = statement.executeQuery();
            NamedPreparedStatement countStatement = getCountByStatusStatementFromDate(connection, strCountQuery, status, fromDate);
            ResultSet resultCount = countStatement.executeQuery()) {
                resultCount.next();
                return new Page<>(fetchDTOList(resultSet), resultCount.getInt(1));
            }
        });
    }
    private NamedPreparedStatement getPageByStatusStatementFromDate(Connection connection, String query, ReservationStatus status, LocalDate fromDate, Integer offset, Integer size) throws SQLException {
        NamedPreparedStatement statement = NamedPreparedStatement.create(connection, query);
        statement.setStatement("status", status.name());
        statement.setStatement("fromDate", Date.valueOf(fromDate));
        statement.setStatement("offset", offset);
        statement.setStatement("size", size);
        return statement;
    }
    private NamedPreparedStatement getCountByStatusStatementFromDate(Connection connection, String query, ReservationStatus status, LocalDate fromDate) throws SQLException {
        NamedPreparedStatement statement = NamedPreparedStatement.create(connection, query);
        statement.setStatement("status", status.name());
        statement.setStatement("fromDate", Date.valueOf(fromDate));
        return statement;
    }

    @Override
    public Page<ReservationDTO> findActiveByRoomClassNameAndArrivalDate(String roomClassName, LocalDate arrivalDate, Integer page, Integer size) {
        String strQuery;
        String strCountQuery;
        if (!roomClassName.equals("All")) {
            strQuery = getQueryByClassAndMethodName().get("reservation.findActiveByRoomClassNameAndArrivalDate");
            strCountQuery = getQueryByClassAndMethodName().get("reservation.findActiveByRoomClassNameAndArrivalDateCount");
        } else{
            strQuery = getQueryByClassAndMethodName().get("reservation.findActiveByAnyRoomClassNameAndArrivalDate");
            strCountQuery = getQueryByClassAndMethodName().get("reservation.findActiveByAnyRoomClassNameAndArrivalDateCount");
        }
            RepositoryUtils.logQuery(LOG, strQuery, roomClassName, arrivalDate, page, size);
        RepositoryUtils.logQuery(LOG, strCountQuery, roomClassName, arrivalDate);
        return getConnectionManager().withConnection(connection -> {
            try (NamedPreparedStatement statement = getPageActiveByRoomClassAndArrivalDateStatement(connection, strQuery, roomClassName, arrivalDate, (page - 1) * size, size);
                 ResultSet resultSet = statement.executeQuery();
                 NamedPreparedStatement countStatement = getCountActiveByRoomClassAndArrivalDateStatement(connection, strCountQuery, roomClassName, arrivalDate);
                 ResultSet resultCount = countStatement.executeQuery()) {
                resultCount.next();
                return new Page<>(fetchDTOList(resultSet), resultCount.getInt(1));
            }
        });
    }

    private NamedPreparedStatement getCountActiveByRoomClassAndArrivalDateStatement(Connection connection, String strCountQuery, String roomClassName, LocalDate arrivalDate) throws SQLException {
        NamedPreparedStatement statement = NamedPreparedStatement.create(connection, strCountQuery);
        statement.setStatement("roomClassName", roomClassName);
        statement.setStatement("arrivalDate", Date.valueOf(arrivalDate));
        return statement;
    }

    private NamedPreparedStatement getPageActiveByRoomClassAndArrivalDateStatement(Connection connection, String strQuery, String roomClassName, LocalDate arrivalDate, int offset, Integer size) throws SQLException {
        NamedPreparedStatement statement = NamedPreparedStatement.create(connection, strQuery);
        statement.setStatement("roomClassName", roomClassName);
        statement.setStatement("arrivalDate", Date.valueOf(arrivalDate));
        statement.setStatement("offset", offset);
        statement.setStatement("size", size);
        return statement;
    }

    @Override
    public List<ReservationDTO> findActiveByUserId(Integer userId) {
        String strQuery = getQueryByClassAndMethodName().get("reservation.findActiveByUserId");
        RepositoryUtils.logQuery(LOG, strQuery, userId);
        return getConnectionManager().withConnection(connection -> {
            try (NamedPreparedStatement statement = getFindActiveByUserIdStatement(connection, strQuery, userId);
                 ResultSet resultSet = statement.executeQuery()) {

                return fetchDTOList(resultSet);
            }
        });
    }

    private NamedPreparedStatement getFindActiveByUserIdStatement(Connection connection, String query, Integer userId) throws SQLException {
        NamedPreparedStatement statement = NamedPreparedStatement.create(connection, query);
        statement.setStatement("userId", userId);
        statement.setStatement("status", ReservationStatus.REJECTED.name());
        return statement;
    }

    @Override
    public Reservation create(Reservation reservation) {
        String strQuery = getQueryByClassAndMethodName().get("reservation.create");
        RepositoryUtils.logQuery(LOG, strQuery, reservation);
        return getCrudHelper().create(
                    DomainToStatementExtractor::extract,
                    strQuery, reservation);
    }

    @Override
    public Optional<Reservation> getById(Integer id) {
        String strQuery = getQueryByClassAndMethodName().get("reservation.getById");
        RepositoryUtils.logQuery(LOG, strQuery, id);
        return getCrudHelper().getById(StatementToDomainExtractor::extractReservation, strQuery, id);
    }


    @Override
    public boolean update(Integer id, Reservation entity) {
        String strQuery;
        if (entity.getRoom() != null)
            strQuery = getQueryByClassAndMethodName().get("reservation.update_withRoom");
        else
            strQuery = getQueryByClassAndMethodName().get("reservation.update_withoutRoom");
        RepositoryUtils.logQuery(LOG, strQuery, id, entity);
        return getCrudHelper().update(DomainToStatementExtractor::extract, strQuery, entity, id);
    }

    @Override
    public boolean delete(Integer id) {
        String strQuery = getQueryByClassAndMethodName().get("reservation.delete");
        RepositoryUtils.logQuery(LOG, strQuery, id);
        return getCrudHelper().delete(strQuery, id);
    }


    public ConnectionManager getConnectionManager() {
        if (connectionManager == null) {
            connectionManager = ApplicationContextHolder.getContext().getBean(ConnectionManager.class);
        }
        return connectionManager;
    }

    public JdbcCrudHelper getCrudHelper() {
        if (crudHelper == null) {
            crudHelper = ApplicationContextHolder.getContext().getBean(JdbcCrudHelper.class);
        }
        return crudHelper;
    }

    public Map<String, String> getQueryByClassAndMethodName() {
        if (queryByClassAndMethodName == null) {
            queryByClassAndMethodName = ApplicationContextHolder.getContext().getBean("queryByClassAndMethodName");
        }
        return queryByClassAndMethodName;
    }
}
