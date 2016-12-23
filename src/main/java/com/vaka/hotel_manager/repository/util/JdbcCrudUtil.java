package com.vaka.hotel_manager.repository.util;

import com.vaka.hotel_manager.domain.BaseEntity;
import com.vaka.hotel_manager.util.exception.RepositoryException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

/**
 * Util that contains duplicates of crud util methods which may be moved
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JdbcCrudUtil {


    /**
     * @param function function must set all params from T to given NamedPreparedStatement
     */
    public static <T extends BaseEntity> boolean update(SQLBiFunction<T, NamedPreparedStatement, NamedPreparedStatement> function,
                                                        DataSource dataSource, String strQuery, T entity, Integer id) throws SQLException {
        entity.setId(id);
        try (Connection connection = dataSource.getConnection();
             NamedPreparedStatement statement = createUpdateStatement(function, connection, strQuery, entity)) {
            return statement.executeUpdate() != 0;
        }
    }

    private static<T> NamedPreparedStatement createUpdateStatement(SQLBiFunction<T, NamedPreparedStatement, NamedPreparedStatement> function,
                                                                   Connection connection, String strQuery, T entity) throws SQLException {
        NamedPreparedStatement statement = new NamedPreparedStatement(connection, strQuery).init();
        function.apply(entity, statement);
        return statement;
    }

    /**
     * @param function must set all params from T to given NamedPreparedStatement
     */
    public static <T extends BaseEntity> T create(SQLBiFunction<T, NamedPreparedStatement, NamedPreparedStatement> function,
                                                  DataSource dataSource, String strQuery, T t) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             NamedPreparedStatement statement = createAndExecuteCreateStatement(connection, strQuery, t, function);
             ResultSet resultSet = statement.getGenerationKeys()) {
            if (resultSet.next()) {
                t.setId(resultSet.getInt(1));
                return t;
            } else throw new SQLException("ID wasn't returned");

        }
    }


    private static<T> NamedPreparedStatement createAndExecuteCreateStatement(Connection connection, String strQuery, T entity,
                                                                                                SQLBiFunction<T,NamedPreparedStatement, NamedPreparedStatement> function) throws SQLException {
        NamedPreparedStatement statement = new NamedPreparedStatement(connection, strQuery, Statement.RETURN_GENERATED_KEYS).init();
        function.apply(entity, statement);
        statement.execute();
        return statement;
    }
    /**
     * @param function must extract T from resultSet
     */
    public static <T> Optional<T> getById(SQLFunction<ResultSet, T> function, DataSource dataSource, String strQuery, Integer id) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             NamedPreparedStatement statement = JdbcCrudUtil.createGetByIdStatement(connection, strQuery, id);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next())
                return Optional.of(function.apply(resultSet));
            else return Optional.empty();
        }
    }

    public static NamedPreparedStatement createGetByIdStatement(Connection connection, String strQuery, Integer id) throws SQLException {
        NamedPreparedStatement statement = new NamedPreparedStatement(connection, strQuery).init();
        statement.setStatement("id", id);
        return statement;
    }


    public static boolean delete(DataSource datasource, String strQuery, Integer id) {
        try (Connection connection = datasource.getConnection();
             NamedPreparedStatement statement = createDeleteStatement(connection, strQuery, id)) {
            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    private static NamedPreparedStatement createDeleteStatement(Connection connection, String strQuery, Integer id) throws SQLException {
        NamedPreparedStatement statement = new NamedPreparedStatement(connection, strQuery).init();
        statement.setStatement("id", id);
        return statement;
    }

}
