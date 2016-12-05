package com.vaka.repository.jdbcImpl;

import com.vaka.context.ApplicationContext;
import com.vaka.domain.User;
import com.vaka.repository.UserRepository;
import com.vaka.util.DomainExtractor;
import com.vaka.util.NamedPreparedStatement;
import com.vaka.util.StatementExtractor;
import com.vaka.util.exception.RepositoryException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

/**
 * Created by Iaroslav on 12/3/2016.
 */
public class UserRepositoryJdbcImpl implements UserRepository {
    private DataSource dataSource;

    @Override
    public Optional<User> getByEmail(String email) {
        String strQuery = "SELECT * FROM user WHERE email = :email";

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
        String strQuery = "INSERT INTO user (created_datetime, email, password, name, role, phone_number) VALUES (:createdDatetime , :email , :password , :name , :role , :phoneNumber )";
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
        String strQuery = "SELECT * FROM user WHERE id = :id";
        try (Connection connection = getDataSource().getConnection();
             NamedPreparedStatement statement = createGetByIdStatement(connection, strQuery, id);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next())
                return Optional.of(DomainExtractor.extractUser(resultSet));
            else return Optional.empty();
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    private NamedPreparedStatement createGetByIdStatement(Connection connection, String strQuery, Integer id) throws SQLException {
        NamedPreparedStatement statement = new NamedPreparedStatement(connection, strQuery).init();
        statement.setStatement("id", id);
        return statement;
    }

    @Override
    public boolean delete(Integer id) {
        String strQuery = "DELETE FROM user WHERE id = :id";
        try (Connection connection = getDataSource().getConnection();
             NamedPreparedStatement statement = createDeleteStatement(connection, strQuery, id)) {
            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    private NamedPreparedStatement createDeleteStatement(Connection connection, String strQuery, Integer id) throws SQLException {
        NamedPreparedStatement statement = new NamedPreparedStatement(connection, strQuery).init();
        statement.setStatement("id", id);
        return statement;
    }

    @Override
    public boolean update(Integer id, User entity) {
        entity.setId(id);
        String strQuery = "UPDATE user u SET email = :email, name = :name, password = :password, role = :role, phone_number = :phoneNumber WHERE u.id = :id";
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
}