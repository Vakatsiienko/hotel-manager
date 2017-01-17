package com.vaka.hotel_manager.repository.jdbcImpl;

import com.vaka.hotel_manager.core.context.ApplicationContext;
import com.vaka.hotel_manager.core.tx.ConnectionProvider;
import com.vaka.hotel_manager.domain.DTO.ReservationDTO;
import com.vaka.hotel_manager.domain.Reservation;
import com.vaka.hotel_manager.domain.ReservationStatus;
import com.vaka.hotel_manager.repository.ReservationRepository;
import com.vaka.hotel_manager.repository.util.JdbcCrudHelper;
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
    private ConnectionProvider connectionProvider;
    private JdbcCrudHelper crudHelper;
    private Map<String, String> queryByClassAndMethodName;

    @Override
    public boolean existOverlapReservations(Integer roomId, LocalDate arrivalDate, LocalDate departureDate) {
        String strQuery = getQueryByClassAndMethodName().get("reservation.existOverlapReservation");
        try (NamedPreparedStatement statement = getExistOverlapReservationStatement(getConnectionProvider().getConnection(), strQuery, roomId, arrivalDate, departureDate);
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
        try (NamedPreparedStatement statement = getFindByUserIdAndStatusStatement(getConnectionProvider().getConnection(), strQuery, userId, status);
             ResultSet resultSet = statement.executeQuery();) {

            return fetchDTOList(resultSet);
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
        String strQuery = getQueryByClassAndMethodName().get("reservation.findByStatusFromDate");
        try (NamedPreparedStatement statement = getStatusStatementFromDate(getConnectionProvider().getConnection(), strQuery, status, fromDate);
             ResultSet resultSet = statement.executeQuery()) {

            return fetchDTOList(resultSet);
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
        String strQuery = getQueryByClassAndMethodName().get("reservation.findActiveByUserId");
        try (NamedPreparedStatement statement = getFindActiveByUserIdStatement(getConnectionProvider().getConnection(), strQuery, userId);
             ResultSet resultSet = statement.executeQuery()) {

            return fetchDTOList(resultSet);
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
            return getCrudHelper().create(
                    DomainToStatementExtractor::extract,
                    strQuery, reservation);
        } catch (SQLException e) {
            LOG.info(e.getMessage());
            throw new RepositoryException(e);
        }
    }

    @Override
    public Optional<Reservation> getById(Integer id) {
        String strQuery = getQueryByClassAndMethodName().get("reservation.getById");
        try {
            return getCrudHelper().getById(StatementToDomainExtractor::extractReservation, strQuery, id);
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
            return getCrudHelper().update(DomainToStatementExtractor::extract, strQuery, entity, id);
        } catch (SQLException e) {
            LOG.info(e.getMessage());
            throw new RepositoryException(e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        String strQuery = getQueryByClassAndMethodName().get("reservation.delete");
        try {
            return getCrudHelper().delete(strQuery, id);
        } catch (SQLException e) {
            LOG.info(e.getMessage());
            throw new RepositoryException(e);
        }
    }



    public ConnectionProvider getConnectionProvider() {
        if (connectionProvider == null) {
            synchronized (this) {
                if (connectionProvider == null) {
                    connectionProvider = ApplicationContext.getInstance().getBean(ConnectionProvider.class);
                }
            }
        }
        return connectionProvider;
    }

    public JdbcCrudHelper getCrudHelper() {
        if (crudHelper == null) {
            synchronized (this) {
                if (crudHelper == null) {
                    crudHelper = ApplicationContext.getInstance().getBean(JdbcCrudHelper.class);
                }
            }
        }
        return crudHelper;
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
