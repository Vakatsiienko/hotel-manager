package com.vaka.hotel_manager.repository.jdbcImpl;

import com.vaka.hotel_manager.core.context.ApplicationContextHolder;
import com.vaka.hotel_manager.core.tx.ConnectionManager;
import com.vaka.hotel_manager.domain.entity.User;
import com.vaka.hotel_manager.repository.UserRepository;
import com.vaka.hotel_manager.repository.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Iaroslav on 12/3/2016.
 */
public class UserRepositoryJdbcImpl implements UserRepository {
    private static final Logger LOG = LoggerFactory.getLogger(UserRepositoryJdbcImpl.class);
    private Map<String, String> queryByClassAndMethodName;
    private ConnectionManager connectionManager;
    private JdbcCrudHelper crudHelper;

    @Override
    public Optional<User> getByVkId(Integer vkId) {
        String strQuery = getQueryByClassAndMethodName().get("user.getByVkId");
        RepositoryUtils.logQuery(LOG, strQuery, vkId);
        return getConnectionManager().withConnection(connection -> {
            try (NamedPreparedStatement statement = createGetByVkIdStatement(connection, strQuery, vkId);
                 ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next())
                    return Optional.of(StatementToDomainExtractor.extractUser(resultSet));
                else return Optional.empty();
            }
        });
    }

    private NamedPreparedStatement createGetByVkIdStatement(Connection connection, String strQuery, Integer vkId) throws SQLException {
        NamedPreparedStatement statement = NamedPreparedStatement.create(connection, strQuery);
        statement.setStatement("userVkId", vkId);
        return statement;
    }

    @Override
    public Optional<User> getByEmail(String email) {
        String strQuery = getQueryByClassAndMethodName().get("user.getByEmail");
        RepositoryUtils.logQuery(LOG, strQuery, email);
        return getConnectionManager().withConnection(connection -> {
            try (NamedPreparedStatement statement = createGetByEmailStatement(connection, strQuery, email);
                 ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next())
                    return Optional.of(StatementToDomainExtractor.extractUser(resultSet));
                else return Optional.empty();
            }
        });
    }

    private NamedPreparedStatement createGetByEmailStatement(Connection connection, String strQuery, String email) throws SQLException {
        NamedPreparedStatement statement = NamedPreparedStatement.create(connection, strQuery);
        statement.setStatement("email", email);
        return statement;
    }

    @Override
    public User create(User entity) {
        String strQuery;
        if (entity.getVkId() == null) {
            strQuery = getQueryByClassAndMethodName().get("user.createWithoutVk");
        } else strQuery = getQueryByClassAndMethodName().get("user.create");
        RepositoryUtils.logQuery(LOG, strQuery, entity);
        return getCrudHelper().create(
                DomainToStatementExtractor::extract,
                strQuery, entity);
    }

    @Override
    public Optional<User> getById(Integer id) {
        String strQuery = getQueryByClassAndMethodName().get("user.getById");
        RepositoryUtils.logQuery(LOG, strQuery, id);
        return getCrudHelper().getById(StatementToDomainExtractor::extractUser, strQuery, id);
    }


    @Override
    public boolean delete(Integer id) {
        String strQuery = getQueryByClassAndMethodName().get("user.delete");
        RepositoryUtils.logQuery(LOG, strQuery, id);
        return getCrudHelper().delete(strQuery, id);
    }


    @Override
    public boolean update(Integer id, User entity) {
        String strQuery = getQueryByClassAndMethodName().get("user.update");
        RepositoryUtils.logQuery(LOG, strQuery, id, entity);
        return getCrudHelper().update(DomainToStatementExtractor::extract, strQuery, entity, id);
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
