package com.vaka.hotel_manager.repository.jdbcImpl;

import com.vaka.hotel_manager.core.context.ApplicationContext;
import com.vaka.hotel_manager.core.tx.ConnectionProvider;
import com.vaka.hotel_manager.domain.User;
import com.vaka.hotel_manager.repository.UserRepository;
import com.vaka.hotel_manager.repository.util.JdbcCrudHelper;
import com.vaka.hotel_manager.repository.util.StatementToDomainExtractor;
import com.vaka.hotel_manager.util.exception.RepositoryException;
import com.vaka.hotel_manager.repository.util.NamedPreparedStatement;
import com.vaka.hotel_manager.repository.util.DomainToStatementExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
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
    private ConnectionProvider connectionProvider;
    private JdbcCrudHelper crudHelper;

    @Override
    public Optional<User> getByEmail(String email) {
        String strQuery = getQueryByClassAndMethodName().get("user.getByEmail");

        try (NamedPreparedStatement statement = createGetByEmailStatement(getConnectionProvider().getConnection(), strQuery, email);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next())
                return Optional.of(StatementToDomainExtractor.extractUser(resultSet));
            else return Optional.empty();
        } catch (SQLException e) {
            LOG.info(e.getMessage());
            throw new RepositoryException(e);
        }
    }

    private NamedPreparedStatement createGetByEmailStatement(Connection connection, String strQuery, String email) throws SQLException {
        NamedPreparedStatement statement = new NamedPreparedStatement(connection, strQuery).init();
        statement.setStatement("email", email);
        return statement;
    }

    @Override
    public User create(User entity) {
        String strQuery = getQueryByClassAndMethodName().get("user.create");
        try {
            return getCrudHelper().create(
                    DomainToStatementExtractor::extract,
                    strQuery, entity);
        } catch (SQLException e) {
            LOG.info(e.getMessage());
            throw new RepositoryException(e);
        }
    }

    @Override
    public Optional<User> getById(Integer id) {
        String strQuery = getQueryByClassAndMethodName().get("user.getById");
        try {
            return getCrudHelper().getById(StatementToDomainExtractor::extractUser, strQuery, id);
        } catch (SQLException e) {
            LOG.info(e.getMessage());
            throw new RepositoryException(e);
        }
    }


    @Override
    public boolean delete(Integer id) {
        String strQuery = getQueryByClassAndMethodName().get("user.delete");
        try {
            return getCrudHelper().delete(strQuery, id);
        } catch (SQLException e) {
            LOG.info(e.getMessage());
            throw new RepositoryException(e);
        }
    }


    @Override
    public boolean update(Integer id, User entity) {
        String strQuery = getQueryByClassAndMethodName().get("user.update");
        try {
            return getCrudHelper().update(DomainToStatementExtractor::extract, strQuery, entity, id);
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
