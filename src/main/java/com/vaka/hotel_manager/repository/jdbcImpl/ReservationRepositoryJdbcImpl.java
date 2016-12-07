package com.vaka.hotel_manager.repository.jdbcImpl;

import com.vaka.hotel_manager.context.ApplicationContext;
import com.vaka.hotel_manager.domain.Reservation;
import com.vaka.hotel_manager.domain.ReservationStatus;
import com.vaka.hotel_manager.repository.ReservationRepository;
import com.vaka.hotel_manager.util.DomainExtractor;
import com.vaka.hotel_manager.util.exception.RepositoryException;
import com.vaka.hotel_manager.util.repository.CrudRepositoryUtil;
import com.vaka.hotel_manager.util.repository.NamedPreparedStatement;
import com.vaka.hotel_manager.util.repository.StatementExtractor;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Iaroslav on 12/4/2016.
 */
public class ReservationRepositoryJdbcImpl implements ReservationRepository {
    private DataSource dataSource;
    private Map<String, String> queryByClassAndMethodName;


    @Override
    public List<Reservation> findByRoomIdAndStatus(Integer roomId, ReservationStatus status) {
        String strCountRowsQuery = getQueryByClassAndMethodName().get("reservation.findByRoomIdAndStatus_count");
        String strQuery = getQueryByClassAndMethodName().get("reservation.findByRoomIdAndStatus");
        try (Connection connection = getDataSource().getConnection();
             NamedPreparedStatement statement = getFindByRoomIdAndStatusStatement(connection, strQuery, roomId, status);
             ResultSet resultSet = statement.executeQuery();
             NamedPreparedStatement countStatement = getFindByRoomIdAndStatusStatement(connection, strCountRowsQuery, roomId, status);
             ResultSet countSet = countStatement.executeQuery();) {

            return fetchListFromResultSet(resultSet, countSet);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    private NamedPreparedStatement getFindByRoomIdAndStatusStatement(Connection connection, String query, Integer roomId, ReservationStatus status) throws SQLException {
        NamedPreparedStatement statement = new NamedPreparedStatement(connection, query).init();
        statement.setStatement("roomId", roomId);
        statement.setStatement("status", status.name());
        return statement;
    }

    private List<Reservation> fetchListFromResultSet(ResultSet resultSet, ResultSet resultCountSet) throws SQLException {
        int count = 0;
        if (resultCountSet.next())
            count = resultCountSet.getInt(1);

        List<Reservation> reservations = new ArrayList<>(count);
        while (resultSet.next()) {
            reservations.add(DomainExtractor.extractReservation(resultSet));
        }
        return reservations;
    }

    @Override
    public List<Reservation> findByUserIdAndStatus(Integer userId, ReservationStatus status) {
        String strCountRowsQuery = getQueryByClassAndMethodName().get("reservation.findByUserIdAndStatus_count");
        String strQuery = getQueryByClassAndMethodName().get("reservation.findByUserIdAndStatus");
        try (Connection connection = getDataSource().getConnection();
             NamedPreparedStatement statement = getFindByUserIdAndStatusStatement(connection, strQuery, userId, status);
             ResultSet resultSet = statement.executeQuery();
             NamedPreparedStatement countStatement = getFindByUserIdAndStatusStatement(connection, strCountRowsQuery, userId, status);
             ResultSet countSet = countStatement.executeQuery()) {

            return fetchListFromResultSet(resultSet, countSet);
        } catch (SQLException e) {
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
    public List<Reservation> findByStatus(ReservationStatus status) {
        String strCountRowsQuery = getQueryByClassAndMethodName().get("reservation.findByStatus_count");
        String strQuery = getQueryByClassAndMethodName().get("reservation.findByStatus");
        try (Connection connection = getDataSource().getConnection();
             NamedPreparedStatement countStatement = getStatusStatement(connection, strCountRowsQuery, status);
             ResultSet countSet = countStatement.executeQuery();
             NamedPreparedStatement statement = getStatusStatement(connection, strQuery, status);
             ResultSet resultSet = statement.executeQuery()) {

            return fetchListFromResultSet(resultSet, countSet);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    private NamedPreparedStatement getStatusStatement(Connection connection, String query, ReservationStatus status) throws SQLException {
        NamedPreparedStatement statement = new NamedPreparedStatement(connection, query).init();
        statement.setStatement("status", status.name());
        return statement;
    }

    @Override
    public List<Reservation> findActiveByUserId(Integer userId) {
        String strCountRowsQuery = getQueryByClassAndMethodName().get("reservation.findActiveByUserId_count");
        String strQuery = getQueryByClassAndMethodName().get("reservation.findActiveByUserId");
        try (Connection connection = getDataSource().getConnection();
             NamedPreparedStatement statement = getFindActiveByUserIdStatement(connection, strQuery, userId);
             ResultSet resultSet = statement.executeQuery();
             NamedPreparedStatement countStatement = getFindActiveByUserIdStatement(connection, strCountRowsQuery, userId);
             ResultSet countSet = countStatement.executeQuery()) {

            return fetchListFromResultSet(resultSet, countSet);
        } catch (SQLException e) {
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
        String strQueryStrategy1 = getQueryByClassAndMethodName().get("reservation.create");
        try (Connection connection = getDataSource().getConnection();
             NamedPreparedStatement statement = createAndExecuteCreateStatement(connection, strQueryStrategy1, reservation, Statement.RETURN_GENERATED_KEYS);
             ResultSet resultSet = statement.getGenerationKeys()) {
            if (resultSet.next())
                reservation.setId(resultSet.getInt(1));
            return reservation;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    private NamedPreparedStatement createAndExecuteCreateStatement(Connection connection, String strQuery, Reservation entity, int statementCode) throws SQLException {
        NamedPreparedStatement statement = new NamedPreparedStatement(connection, strQuery, statementCode).init();
        StatementExtractor.extract(entity, statement);
        statement.execute();
        return statement;
    }

    @Override
    public Optional<Reservation> getById(Integer id) {
        String strQuery = getQueryByClassAndMethodName().get("reservation.getById");
        try (Connection connection = getDataSource().getConnection();
             NamedPreparedStatement statement = CrudRepositoryUtil.createGetByIdStatement(connection, strQuery, id);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next())
                return Optional.of(DomainExtractor.extractReservation(resultSet));
            else return Optional.empty();
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }


    @Override
    public boolean update(Integer id, Reservation entity) {
        entity.setId(id);
        String strQuery;
        if (entity.getRoom() != null)
            strQuery = getQueryByClassAndMethodName().get("reservation.update_withRoom");
        else
            strQuery = getQueryByClassAndMethodName().get("reservation.update_withoutRoom");
        //TODO add strategies
        try (Connection connection = getDataSource().getConnection();
             NamedPreparedStatement statement = createUpdateStatement(connection, strQuery, entity)) {
            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    private NamedPreparedStatement createUpdateStatement(Connection connection, String strQuery, Reservation entity) throws SQLException {
        NamedPreparedStatement statement = new NamedPreparedStatement(connection, strQuery).init();
        StatementExtractor.extract(entity, statement);
        return statement;
    }

    @Override
    public boolean delete(Integer id) {
        String strQuery = getQueryByClassAndMethodName().get("reservation.delete");
        return CrudRepositoryUtil.delete(strQuery, id);
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
