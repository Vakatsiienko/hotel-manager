package com.vaka.hotel_manager.repository.util;

import com.vaka.hotel_manager.core.context.ApplicationContext;
import com.vaka.hotel_manager.core.tx.ConnectionManager;
import com.vaka.hotel_manager.domain.entity.BaseEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

/**
 * Util that contains duplicates of crud util methods which may be moved
 */
@AllArgsConstructor
public class JdbcCrudHelper {

    private ConnectionManager connectionManager;
    /**
     * @param function function must set all params from T to given NamedPreparedStatement
     */
    public <T extends BaseEntity> boolean update(SQLBiFunction<T, NamedPreparedStatement, NamedPreparedStatement> function,
                                                        String strQuery, T entity, Integer id) {
        return getConnectionManager().withConnection(connection -> {
            entity.setId(id);
            try (NamedPreparedStatement statement = createUpdateStatement(function, connection, strQuery, entity)) {
                return statement.executeUpdate() != 0;
            }
        });
    }

    private <T extends BaseEntity> NamedPreparedStatement createUpdateStatement(SQLBiFunction<T, NamedPreparedStatement, NamedPreparedStatement> function,
                                                                    Connection connection, String strQuery, T entity) throws SQLException {
        NamedPreparedStatement statement = NamedPreparedStatement.create(connection, strQuery);
        function.apply(entity, statement);
        statement.setStatement("id", entity.getId());
        return statement;
    }

    /**
     * @param function must set all params from T to given NamedPreparedStatement
     */
    public <T extends BaseEntity> T create(SQLBiFunction<T, NamedPreparedStatement, NamedPreparedStatement> function,
                                                  String strQuery, T t) {
        return getConnectionManager().withConnection(connection -> {
            try (NamedPreparedStatement statement = createAndExecuteCreateStatement(connection, strQuery, t, function);
                 ResultSet resultSet = statement.getGenerationKeys()) {
                if (resultSet.next()) {
                    t.setId(resultSet.getInt(1));
                    return t;
                } else throw new SQLException("ID wasn't returned");

            }
        });
    }


    private <T> NamedPreparedStatement createAndExecuteCreateStatement(Connection connection, String strQuery, T entity,
                                                                              SQLBiFunction<T, NamedPreparedStatement, NamedPreparedStatement> function) throws SQLException {
        NamedPreparedStatement statement = NamedPreparedStatement.create(connection, strQuery, Statement.RETURN_GENERATED_KEYS);
        function.apply(entity, statement);
        statement.execute();
        return statement;
    }

    /**
     * @param function must extract T from resultSet
     */
    public <T> Optional<T> getById(SQLFunction<ResultSet, T> function, String strQuery, Integer id) {
        return getConnectionManager().withConnection(connection -> {
            try (NamedPreparedStatement statement = createGetByIdStatement(connection, strQuery, id);
                 ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next())
                    return Optional.of(function.apply(resultSet));
                else return Optional.empty();
            }
        });
    }

    public NamedPreparedStatement createGetByIdStatement(Connection connection, String strQuery, Integer id) throws SQLException {
        NamedPreparedStatement statement = NamedPreparedStatement.create(connection, strQuery);
        statement.setStatement("id", id);
        return statement;
    }


    public boolean delete(String strQuery, Integer id) {
        return getConnectionManager().withConnection(connection -> {
            try (NamedPreparedStatement statement = createDeleteStatement(connection, strQuery, id)) {
                return statement.executeUpdate() != 0;
            }
        });
    }

    private NamedPreparedStatement createDeleteStatement(Connection connection, String strQuery, Integer id) throws SQLException {
        NamedPreparedStatement statement = NamedPreparedStatement.create(connection, strQuery);
        statement.setStatement("id", id);
        return statement;
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
}
