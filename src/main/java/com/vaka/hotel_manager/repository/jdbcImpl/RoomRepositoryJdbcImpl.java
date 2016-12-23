package com.vaka.hotel_manager.repository.jdbcImpl;

import com.vaka.hotel_manager.context.ApplicationContext;
import com.vaka.hotel_manager.domain.Room;
import com.vaka.hotel_manager.domain.RoomClass;
import com.vaka.hotel_manager.repository.RoomRepository;
import com.vaka.hotel_manager.repository.util.JdbcCrudUtil;
import com.vaka.hotel_manager.repository.util.StatementToDomainExtractor;
import com.vaka.hotel_manager.util.exception.RepositoryException;
import com.vaka.hotel_manager.repository.util.NamedPreparedStatement;
import com.vaka.hotel_manager.repository.util.DomainToStatementExtractor;
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
 * Created by Iaroslav on 12/3/2016.
 */
public class RoomRepositoryJdbcImpl implements RoomRepository {
    private static final Logger LOG = LoggerFactory.getLogger(RoomRepositoryJdbcImpl.class);
    private DataSource dataSource;
    private Map<String, String> queryByClassAndMethodName;

    @Override
    public List<Room> findAll() {
        String strQuery = getQueryByClassAndMethodName().get("room.findAll");
        String strCountQuery = getQueryByClassAndMethodName().get("room.findAll_count");
        try (Connection connection = getDataSource().getConnection();
             NamedPreparedStatement statement = new NamedPreparedStatement(connection, strQuery).init();
             ResultSet resultSet = statement.executeQuery();
             NamedPreparedStatement countStatement = new NamedPreparedStatement(connection, strCountQuery).init();
             ResultSet countResultSet = countStatement.executeQuery()) {
            return fetchToList(resultSet, countResultSet);
        } catch (SQLException e) {
            LOG.info(e.getMessage());
            throw new RepositoryException(e);
        }
    }

    private List<Room> fetchToList(ResultSet resultSet, ResultSet countSet) throws SQLException {
        int size = 0;
        if (countSet.next())
            size = countSet.getInt(1);
        List<Room> rooms = new ArrayList<>(size);
        while (resultSet.next()) {
            rooms.add(StatementToDomainExtractor.extractRoom(resultSet));
        }
        return rooms;
    }

    @Override
    public List<Room> findAvailableForReservation(RoomClass roomClass, LocalDate arrivalDate, LocalDate departureDate) {
        String strQuery = getQueryByClassAndMethodName().get("room.findAvailableForReservation");
        String strCountQuery = getQueryByClassAndMethodName().get("room.findAvailableForReservation_count");

        try (Connection connection = getDataSource().getConnection();
             NamedPreparedStatement statement = getFindAvailableForReservationStatement(connection, strQuery, roomClass, arrivalDate, departureDate);
             ResultSet resultSet = statement.executeQuery();
             NamedPreparedStatement countStatement = getFindAvailableForReservationStatement(connection, strCountQuery, roomClass, arrivalDate, departureDate);
             ResultSet countResultSet = countStatement.executeQuery()) {
            return fetchToList(resultSet, countResultSet);
        } catch (SQLException e) {
            LOG.info(e.getMessage());
            throw new RepositoryException(e);
        }
    }

    private NamedPreparedStatement getFindAvailableForReservationStatement(Connection connection, String strQuery, RoomClass roomClass, LocalDate arrivalDate, LocalDate departureDate) throws SQLException {
        NamedPreparedStatement statement = new NamedPreparedStatement(connection, strQuery).init();
        statement.setStatement("roomClass", roomClass.name());
        statement.setStatement("arrivalDate", Date.valueOf(arrivalDate));
        statement.setStatement("departureDate", Date.valueOf(departureDate));
        return statement;
    }

    @Override
    public Room create(Room entity) {
        String strQuery = getQueryByClassAndMethodName().get("room.create");
        try {
            return JdbcCrudUtil.create(
                    DomainToStatementExtractor::extract,
                    getDataSource(), strQuery, entity);
        } catch (SQLException e) {
            LOG.info(e.getMessage());
            throw new RepositoryException(e);
        }
    }

    @Override
    public Optional<Room> getById(Integer id) {
        String strQuery = getQueryByClassAndMethodName().get("room.getById");
        try {
            return JdbcCrudUtil.getById(StatementToDomainExtractor::extractRoom, getDataSource(), strQuery, id);
        } catch (SQLException e) {
            LOG.info(e.getMessage());
            throw new RepositoryException(e);
        }
    }


    @Override
    public boolean delete(Integer id) {
        String strQuery = getQueryByClassAndMethodName().get("room.delete");
        return JdbcCrudUtil.delete(getDataSource(), strQuery, id);
    }

    @Override
    public boolean update(Integer id, Room entity) {
        String strQuery = getQueryByClassAndMethodName().get("room.update");
        try {
            return JdbcCrudUtil.update(DomainToStatementExtractor::extract, getDataSource(), strQuery, entity, id);
        } catch (SQLException e) {
            LOG.info(e.getMessage());
            throw new RepositoryException(e);
        }
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
