package com.vaka.hotel_manager.util.repository;

import com.vaka.hotel_manager.context.ApplicationContext;
import com.vaka.hotel_manager.domain.Room;
import com.vaka.hotel_manager.util.exception.RepositoryException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Iaroslav on 12/3/2016.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CrudRepositoryUtil {
    private static DataSource dataSource;

    private static DataSource getDataSource() {
        if (dataSource == null) {
            synchronized (CrudRepositoryUtil.class) {
                if (dataSource == null) {
                    dataSource = ApplicationContext.getInstance().getBean(DataSource.class);
                }
            }
        }
        return dataSource;
    }

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



    public static NamedPreparedStatement createGetByIdStatement(Connection connection, String strQuery, Integer id) throws SQLException {
        NamedPreparedStatement statement = new NamedPreparedStatement(connection, strQuery).init();
        statement.setStatement("id", id);
        return statement;
    }

    public static boolean delete(String strQuery, Integer id) {
        try (Connection connection = getDataSource().getConnection();
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
