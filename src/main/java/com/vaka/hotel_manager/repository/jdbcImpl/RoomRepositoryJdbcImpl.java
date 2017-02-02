package com.vaka.hotel_manager.repository.jdbcImpl;

import com.vaka.hotel_manager.core.context.ApplicationContext;
import com.vaka.hotel_manager.core.tx.ConnectionManager;
import com.vaka.hotel_manager.domain.Page;
import com.vaka.hotel_manager.domain.entity.Room;
import com.vaka.hotel_manager.domain.entity.RoomClass;
import com.vaka.hotel_manager.repository.RoomRepository;
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
 * Created by Iaroslav on 12/3/2016.
 */
public class RoomRepositoryJdbcImpl implements RoomRepository {
    private static final Logger LOG = LoggerFactory.getLogger(RoomRepositoryJdbcImpl.class);
    private ConnectionManager connectionManager;
    private Map<String, String> queryByClassAndMethodName;
    private JdbcCrudHelper crudHelper;

    @Override
    public Page<Room> findPage(Integer page, Integer size) {
        String strQuery = getQueryByClassAndMethodName().get("room.findPage");
        String strCountQuery = getQueryByClassAndMethodName().get("room.totalCount");
        RepositoryUtils.logQuery(LOG, strQuery, page, size);
        RepositoryUtils.logQuery(LOG, strQuery);
        return getConnectionManager().withConnection(connection -> {
            try (NamedPreparedStatement statement = getFindPageStatement(connection, strQuery, (page - 1) * size, size);
                 ResultSet rs = statement.executeQuery();
                 ResultSet rsLength = NamedPreparedStatement.create(connection, strCountQuery).executeQuery()) {
                rsLength.next();
                Integer length = rsLength.getInt(1);
                return new Page<>(fetchToList(rs), length);
            }
        });
    }

    private NamedPreparedStatement getFindPageStatement(Connection connection, String strQuery, Integer page, Integer size) throws SQLException {
        NamedPreparedStatement statement = NamedPreparedStatement.create(connection, strQuery);
        statement.setStatement("offset", page);
        statement.setStatement("size", size);
        return statement;
    }

    @Override
    public List<Room> findAll() {
        String strQuery = getQueryByClassAndMethodName().get("room.findAll");
        RepositoryUtils.logQuery(LOG, strQuery);
        return getConnectionManager().withConnection(connection -> {
            try (NamedPreparedStatement statement = NamedPreparedStatement.create(connection, strQuery);
                 ResultSet resultSet = statement.executeQuery()) {
                return fetchToList(resultSet);
            }
        });
    }

    @Override
    public Optional<Room> getByNumber(Integer number) {
        String strQuery = getQueryByClassAndMethodName().get("room.getByNumber");
        RepositoryUtils.logQuery(LOG, strQuery, number);
        return getConnectionManager().withConnection(connection -> {
            try (NamedPreparedStatement statement = getGetByNumberStatement(connection, strQuery, number);
                 ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next())
                    return Optional.of(StatementToDomainExtractor.extractRoom(resultSet));
                else return Optional.empty();
            }
        });
    }

    private NamedPreparedStatement getGetByNumberStatement(Connection connection, String strQuery, Integer number) throws SQLException {
        NamedPreparedStatement statement = NamedPreparedStatement.create(connection, strQuery);
        statement.setStatement("number", number);
        return statement;
    }

    @Override
    public boolean existsRoomByRoomClass(Integer roomClassId) {
        String strQuery = getQueryByClassAndMethodName().get("room.existsRoomByRoomClass");
        RepositoryUtils.logQuery(LOG, strQuery, roomClassId);
        return getConnectionManager().withConnection(connection -> {
            try (NamedPreparedStatement statement = getExistsRoomByRoomClassStatement(connection, strQuery, roomClassId);
                 ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() && resultSet.getBoolean(1);
            }
        });
    }

    private NamedPreparedStatement getExistsRoomByRoomClassStatement(Connection connection, String strQuery, Integer roomClassId) throws SQLException {
        NamedPreparedStatement statement = NamedPreparedStatement.create(connection, strQuery);
        statement.setStatement("roomClassId", roomClassId);
        return statement;
    }

    private List<Room> fetchToList(ResultSet resultSet) throws SQLException {
        int size = resultSet.getMetaData().getColumnCount();
        List<Room> rooms = new ArrayList<>(size);
        while (resultSet.next()) {
            rooms.add(StatementToDomainExtractor.extractRoom(resultSet));
        }
        return rooms;
    }

    @Override
    public List<Room> findAvailableForReservation(RoomClass roomClass, LocalDate arrivalDate, LocalDate departureDate) {
        String strQuery = getQueryByClassAndMethodName().get("room.findAvailableForReservation");
        RepositoryUtils.logQuery(LOG, strQuery, roomClass, arrivalDate, departureDate);
        return getConnectionManager().withConnection(connection -> {
            try (NamedPreparedStatement statement = getFindAvailableForReservationStatement(connection, strQuery, roomClass, arrivalDate, departureDate);
                 ResultSet resultSet = statement.executeQuery()) {
                return fetchToList(resultSet);
            }
        });
    }

    private NamedPreparedStatement getFindAvailableForReservationStatement(Connection connection, String strQuery, RoomClass roomClass, LocalDate arrivalDate, LocalDate departureDate) throws SQLException {
        NamedPreparedStatement statement = NamedPreparedStatement.create(connection, strQuery);
        statement.setStatement("roomClassName", roomClass.getName());
        statement.setStatement("arrivalDate", Date.valueOf(arrivalDate));
        statement.setStatement("departureDate", Date.valueOf(departureDate));
        return statement;
    }

    @Override
    public Room create(Room entity) {
        String strQuery = getQueryByClassAndMethodName().get("room.create");
        RepositoryUtils.logQuery(LOG, strQuery, entity);
        return getCrudHelper().create(
                    DomainToStatementExtractor::extract,
                    strQuery, entity);

    }

    @Override
    public Optional<Room> getById(Integer id) {
        String strQuery = getQueryByClassAndMethodName().get("room.getById");
        RepositoryUtils.logQuery(LOG, strQuery, id);
        return getCrudHelper().getById(StatementToDomainExtractor::extractRoom, strQuery, id);

    }


    @Override
    public boolean delete(Integer id) {
        String strQuery = getQueryByClassAndMethodName().get("room.delete");
        RepositoryUtils.logQuery(LOG, strQuery, id);
        return getCrudHelper().delete(strQuery, id);
    }

    @Override
    public boolean update(Integer id, Room entity) {
        String strQuery = getQueryByClassAndMethodName().get("room.update");
        RepositoryUtils.logQuery(LOG, strQuery, id, entity);
        return getCrudHelper().update(DomainToStatementExtractor::extract, strQuery, entity, id);

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

    public ConnectionManager getConnectionManager() {
        if (connectionManager == null) {
            synchronized (this) {
                if (connectionManager == null) {
                    connectionManager = ApplicationContext.getInstance().getBean(ConnectionManager.class);
                }
            }
        }
        return connectionManager;
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
}
