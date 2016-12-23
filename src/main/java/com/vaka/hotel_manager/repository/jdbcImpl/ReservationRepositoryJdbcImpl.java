package com.vaka.hotel_manager.repository.jdbcImpl;

import com.vaka.hotel_manager.context.ApplicationContext;
import com.vaka.hotel_manager.domain.DTO.ReservationDTO;
import com.vaka.hotel_manager.domain.Reservation;
import com.vaka.hotel_manager.domain.ReservationStatus;
import com.vaka.hotel_manager.repository.ReservationRepository;
import com.vaka.hotel_manager.repository.util.JdbcCrudUtil;
import com.vaka.hotel_manager.util.exception.RepositoryException;
import com.vaka.hotel_manager.repository.util.DomainToStatementExtractor;
import com.vaka.hotel_manager.repository.util.NamedPreparedStatement;
import com.vaka.hotel_manager.repository.util.StatementToDomainExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
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
    private DataSource dataSource;
    private Map<String, String> queryByClassAndMethodName;

    @Override
    public boolean existOverlapReservation(Integer roomId, LocalDate arrivalDate, LocalDate departureDate) {
        String strQuery = getQueryByClassAndMethodName().get("reservation.existOverlapReservation");
        try (Connection connection = getDataSource().getConnection();
             NamedPreparedStatement statement = getExistOverlapReservationStatement(connection, strQuery, roomId, arrivalDate, departureDate);
             ResultSet resultSet = statement.executeQuery()) {
            resultSet.next();
            return resultSet.getInt(1) != 0;
        } catch (SQLException e) {
            LOG.info(e.getMessage());
            throw new RepositoryException(e);
        }
    }

    private NamedPreparedStatement getExistOverlapReservationStatement(Connection connection, String query, Integer roomId, LocalDate arrivalDate, LocalDate departureDate) throws SQLException {
        NamedPreparedStatement statement = new NamedPreparedStatement(connection, query).init();
        statement.setStatement("roomId", roomId);
        statement.setStatement("arrivalDate", Date.valueOf(arrivalDate));
        statement.setStatement("departureDate", Date.valueOf(departureDate));
        return statement;
    }


    private List<ReservationDTO> fetchDTOList(ResultSet resultSet, ResultSet resultCountSet) throws SQLException {
        int count = 0;
        if (resultCountSet.next())
            count = resultCountSet.getInt(1);

        List<ReservationDTO> reservations = new ArrayList<>(count);
        while (resultSet.next()) {
            reservations.add(StatementToDomainExtractor.extractReservationDTO(resultSet));
        }
        return reservations;
    }

    @Override
    public List<ReservationDTO> findByUserIdAndStatus(Integer userId, ReservationStatus status) {
        String strCountRowsQuery = getQueryByClassAndMethodName().get("reservation.findByUserIdAndStatus_count");
        String strQuery = getQueryByClassAndMethodName().get("reservation.findByUserIdAndStatus");
        try (Connection connection = getDataSource().getConnection();
             NamedPreparedStatement statement = getFindByUserIdAndStatusStatement(connection, strQuery, userId, status);
             ResultSet resultSet = statement.executeQuery();
             NamedPreparedStatement countStatement = getFindByUserIdAndStatusStatement(connection, strCountRowsQuery, userId, status);
             ResultSet countSet = countStatement.executeQuery()) {

            return fetchDTOList(resultSet, countSet);
        } catch (SQLException e) {
            LOG.info(e.getMessage());
            throw new RepositoryException(e);
        }
    }

    private NamedPreparedStatement getFindByUserIdAndStatusStatement(Connection connection, String query, Integer userId, ReservationStatus status) throws SQLException {
        NamedPreparedStatement statement = new NamedPreparedStatement(connection, query).init();
        statement.setStatement("userId", userId);
        statement.setStatement("status", status.name());
        return statement;
    }

    @Override
    public List<ReservationDTO> findByStatusFromDate(ReservationStatus status, LocalDate fromDate) {
        String strCountRowsQuery = getQueryByClassAndMethodName().get("reservation.findByStatusFromDate_count");
        String strQuery = getQueryByClassAndMethodName().get("reservation.findByStatusFromDate");
        try (Connection connection = getDataSource().getConnection();
             NamedPreparedStatement countStatement = getStatusStatementFromDate(connection, strCountRowsQuery, status, fromDate);
             ResultSet countSet = countStatement.executeQuery();
             NamedPreparedStatement statement = getStatusStatementFromDate(connection, strQuery, status, fromDate);
             ResultSet resultSet = statement.executeQuery()) {

            return fetchDTOList(resultSet, countSet);
        } catch (SQLException e) {
            LOG.info(e.getMessage());
            throw new RepositoryException(e);
        }
    }
    private NamedPreparedStatement getStatusStatementFromDate(Connection connection, String query, ReservationStatus status, LocalDate fromDate) throws SQLException {
        NamedPreparedStatement statement = new NamedPreparedStatement(connection, query).init();
        statement.setStatement("status", status.name());
        statement.setStatement("fromDate", Date.valueOf(fromDate));
        return statement;
    }

    @Override
    public List<ReservationDTO> findActiveByUserId(Integer userId) {
        String strCountRowsQuery = getQueryByClassAndMethodName().get("reservation.findActiveByUserId_count");
        String strQuery = getQueryByClassAndMethodName().get("reservation.findActiveByUserId");
        try (Connection connection = getDataSource().getConnection();
             NamedPreparedStatement statement = getFindActiveByUserIdStatement(connection, strQuery, userId);
             ResultSet resultSet = statement.executeQuery();
             NamedPreparedStatement countStatement = getFindActiveByUserIdStatement(connection, strCountRowsQuery, userId);
             ResultSet countSet = countStatement.executeQuery()) {

            return fetchDTOList(resultSet, countSet);
        } catch (SQLException e) {
            LOG.info(e.getMessage());
            throw new RepositoryException(e);
        }
    }

    private NamedPreparedStatement getFindActiveByUserIdStatement(Connection connection, String query, Integer userId) throws SQLException {
        NamedPreparedStatement statement = new NamedPreparedStatement(connection, query).init();
        statement.setStatement("userId", userId);
        statement.setStatement("status", ReservationStatus.REJECTED.name());
        return statement;
    }

    @Override
    public Reservation create(Reservation reservation) {
        String strQuery = getQueryByClassAndMethodName().get("reservation.create");
        try {
            return JdbcCrudUtil.create(
                    DomainToStatementExtractor::extract,
                    getDataSource(), strQuery, reservation);
        } catch (SQLException e) {
            LOG.info(e.getMessage());
            throw new RepositoryException(e);
        }
    }

    @Override
    public Optional<Reservation> getById(Integer id) {
        String strQuery = getQueryByClassAndMethodName().get("reservation.getById");
        try {
            return JdbcCrudUtil.getById(StatementToDomainExtractor::extractReservation, getDataSource(), strQuery, id);
        } catch (SQLException e) {
            LOG.info(e.getMessage());
            throw new RepositoryException(e);
        }
    }


    @Override
    public boolean update(Integer id, Reservation entity) {
        String strQuery;
        if (entity.getRoom() != null)
            strQuery = getQueryByClassAndMethodName().get("reservation.update_withRoom");
        else
            strQuery = getQueryByClassAndMethodName().get("reservation.update_withoutRoom");
        try {
            return JdbcCrudUtil.update(DomainToStatementExtractor::extract, getDataSource(), strQuery, entity, id);
        } catch (SQLException e) {
            LOG.info(e.getMessage());
            throw new RepositoryException(e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        String strQuery = getQueryByClassAndMethodName().get("reservation.delete");
        return JdbcCrudUtil.delete(getDataSource(), strQuery, id);
    }


    public DataSource getDataSource() {
        if (dataSource == null) {
            synchronized (this) {
                if (dataSource == null) {
                    dataSource = ApplicationContext.getInstance().getBean(DataSource.class);
                }
            }
        }
        return dataSource;
    }

    public Map<String, String> getQueryByClassAndMethodName() {
        if (queryByClassAndMethodName == null) {
            synchronized (this) {
                if (queryByClassAndMethodName == null) {
                    queryByClassAndMethodName = ApplicationContext.getInstance().getQueryByClassAndMethodName();
                }
            }
        }
        return queryByClassAndMethodName;
    }
}
