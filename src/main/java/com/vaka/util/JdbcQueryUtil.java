package com.vaka.util;

import com.vaka.domain.BaseEntity;
import com.vaka.domain.Room;
import com.vaka.util.exception.RepositoryException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Iaroslav on 12/3/2016.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JdbcQueryUtil {

//    public static <T> T persist(T entity, String strQuery, DataSource dataSource) {
//        try (Connection connection = dataSource.getConnection();
//             NamedPreparedStatement statement = createAndExecuteCreateStatement(connection, strQuery, entity, Statement.RETURN_GENERATED_KEYS);
//             ResultSet resultSet = statement.getGenerationKeys()) {
//            if (resultSet.next()) {
//                entity.setId(resultSet.getInt(1));
//                return entity;
//            } else throw new SQLException("ID wasn't returned");
//        } catch (SQLException e) {
//            throw new RepositoryException(e);
//        }
//    }
    private static NamedPreparedStatement createAndExecuteCreateStatement(Connection connection, String strQuery, Room entity, int statementCode) throws SQLException {
        NamedPreparedStatement statement = new NamedPreparedStatement(connection, strQuery, statementCode).init();
        StatementExtractor.extract(entity, statement);
        statement.execute();
        return statement;
    }
    public static int getId(Connection connection) throws SQLException {
        String strQuery = "SELECT value FROM jdbc_sequences";
        NamedPreparedStatement statement = new NamedPreparedStatement(connection, strQuery);
        ResultSet resultSet = statement.executeQuery();
        return resultSet.getInt(1);
    }
}
