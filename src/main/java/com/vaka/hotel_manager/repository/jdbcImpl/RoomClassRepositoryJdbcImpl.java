package com.vaka.hotel_manager.repository.jdbcImpl;

import com.vaka.hotel_manager.core.context.ApplicationContext;
import com.vaka.hotel_manager.core.tx.ConnectionManager;
import com.vaka.hotel_manager.domain.dto.RoomClassDTO;
import com.vaka.hotel_manager.domain.entity.RoomClass;
import com.vaka.hotel_manager.repository.RoomClassRepository;
import com.vaka.hotel_manager.repository.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Iaroslav on 1/21/2017.
 */
public class RoomClassRepositoryJdbcImpl implements RoomClassRepository {
    private static final Logger LOG = LoggerFactory.getLogger(RoomClassRepositoryJdbcImpl.class);
    private ConnectionManager connectionManager;
    private JdbcCrudHelper crudHelper;
    private Map<String, String> queryByClassAndMethodName;

    @Override
    public Optional<RoomClass> getByName(String name) {
        String strQuery = getQueryByClassAndMethodName().get("roomClass.getByName");
        RepositoryUtils.logQuery(LOG, strQuery, name);
        return getConnectionManager().withConnection(connection -> {
            try (NamedPreparedStatement statement = getGetByNameStatement(connection, strQuery, name);
                 ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next())
                    return Optional.of(StatementToDomainExtractor.extractRoomClass(resultSet));
                else return Optional.empty();
            }
        });
    }

    private NamedPreparedStatement getGetByNameStatement(Connection connection, String strQuery, String name) throws SQLException {
        NamedPreparedStatement statement = NamedPreparedStatement.create(connection, strQuery);
        statement.setStatement("name", name);
        return statement;
    }

    @Override
    public List<RoomClass> findAll() {
        String strQuery = getQueryByClassAndMethodName().get("roomClass.findAll");
        RepositoryUtils.logQuery(LOG, strQuery);
        return getConnectionManager().withConnection(connection -> {
            try (NamedPreparedStatement statement = NamedPreparedStatement.create(connection, strQuery);
                 ResultSet resultSet = statement.executeQuery()) {
                return fetchToList(resultSet);
            }
        });
    }

    private List<RoomClass> fetchToList(ResultSet resultSet) throws SQLException {
        List<RoomClass> roomClassSet = new ArrayList<>(resultSet.getMetaData().getColumnCount());
        while (resultSet.next()) {
            roomClassSet.add(StatementToDomainExtractor.extractRoomClass(resultSet));
        }
        return roomClassSet;
    }

    private Set<RoomClassDTO> fetchDTOToSet(ResultSet resultSet) throws SQLException {
        Set<RoomClassDTO> roomClassSet = new HashSet<>(resultSet.getMetaData().getColumnCount());
        while (resultSet.next()) {
            roomClassSet.add(new RoomClassDTO(resultSet.getString("name")));
        }
        return roomClassSet;
    }

    @Override
    public RoomClass create(RoomClass entity) {
        String strQuery = getQueryByClassAndMethodName().get("roomClass.create");
        RepositoryUtils.logQuery(LOG, strQuery, entity);
        return getCrudHelper().create(DomainToStatementExtractor::extract, strQuery, entity);

    }

    @Override
    public Optional<RoomClass> getById(Integer id) {
        String strQuery = getQueryByClassAndMethodName().get("roomClass.getById");
        RepositoryUtils.logQuery(LOG, strQuery, id);
        return getCrudHelper().getById(StatementToDomainExtractor::extractRoomClass, strQuery, id);

    }

    @Override
    public boolean delete(Integer id) {
        String strQuery = getQueryByClassAndMethodName().get("roomClass.delete");
        RepositoryUtils.logQuery(LOG, strQuery, id);
        return getCrudHelper().delete(strQuery, id);

    }

    @Override
    public boolean update(Integer id, RoomClass entity) {
        String strQuery = getQueryByClassAndMethodName().get("roomClass.update");
        RepositoryUtils.logQuery(LOG, strQuery, id, entity);
        return getCrudHelper().update(DomainToStatementExtractor::extract, strQuery, entity, id);

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
