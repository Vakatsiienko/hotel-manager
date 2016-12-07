package com.vaka.hotel_manager.repository.jdbcImpl;

import com.vaka.hotel_manager.context.ApplicationContext;
import com.vaka.hotel_manager.domain.User;
import com.vaka.hotel_manager.repository.UserRepository;
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
import java.util.Map;
import java.util.Optional;

/**
 * Created by Iaroslav on 12/3/2016.
 */
public class UserRepositoryJdbcImpl implements UserRepository {
    private DataSource dataSource;
    private Map<String, String> queryByClassAndMethodName;

    @Override
    public Optional<User> getByEmail(String email) {
        String strQuery = getQueryByClassAndMethodName().get("user.getByEmail");

        try (Connection connection = getDataSource().getConnection();
             NamedPreparedStatement statement = createGetByEmailStatement(connection, strQuery, email);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next())
                return Optional.of(DomainExtractor.extractUser(resultSet));
            else return Optional.empty();
        } catch (SQLException e) {
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
        try (Connection connection = getDataSource().getConnection();//TODO move to JdbcUtil
             NamedPreparedStatement statement = createAndExecuteCreateStatement(connection, strQuery, entity, Statement.RETURN_GENERATED_KEYS);
             ResultSet resultSet = statement.getGenerationKeys()) {
            if (resultSet.next()) {
                entity.setId(resultSet.getInt(1));
                return entity;
            } else throw new SQLException("ID wasn't returned");
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    private NamedPreparedStatement createAndExecuteCreateStatement(Connection connection, String strQuery, User entity, int statementCode) throws SQLException {
        NamedPreparedStatement statement = new NamedPreparedStatement(connection, strQuery, statementCode).init();
        StatementExtractor.extract(entity, statement);
        statement.execute();
        return statement;
    }

    @Override
    public Optional<User> getById(Integer id) {
        String strQuery = getQueryByClassAndMethodName().get("user.getById");
        try (Connection connection = getDataSource().getConnection();
             NamedPreparedStatement statement = CrudRepositoryUtil.createGetByIdStatement(connection, strQuery, id);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next())
                return Optional.of(DomainExtractor.extractUser(resultSet));
            else return Optional.empty();
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }


    @Override
    public boolean delete(Integer id) {
        String strQuery = getQueryByClassAndMethodName().get("user.delete");
        return CrudRepositoryUtil.delete(strQuery, id);
    }


    @Override
    public boolean update(Integer id, User entity) {
        entity.setId(id);
        String strQuery = getQueryByClassAndMethodName().get("user.update");
        try (Connection connection = getDataSource().getConnection();
             NamedPreparedStatement statement = createUpdateStatement(connection, strQuery, entity)) {
            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    private NamedPreparedStatement createUpdateStatement(Connection connection, String strQuery, User entity) throws SQLException {
        NamedPreparedStatement statement = new NamedPreparedStatement(connection, strQuery).init();
        StatementExtractor.extract(entity, statement);
        return statement;
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
