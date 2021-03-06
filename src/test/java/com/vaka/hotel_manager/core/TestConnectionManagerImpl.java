package com.vaka.hotel_manager.core;

import com.vaka.hotel_manager.core.context.ApplicationContextHolder;
import com.vaka.hotel_manager.core.tx.ConnectionManager;
import com.vaka.hotel_manager.repository.util.SQLFunction;
import com.vaka.hotel_manager.util.exception.RepositoryException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Iaroslav on 1/30/2017.
 */
public class TestConnectionManagerImpl implements ConnectionManager {

    private DataSource dataSource;

    @Override
    public <T> T withConnection(SQLFunction<Connection, T> withCon) {
        try (Connection connection = getDataSource().getConnection()) {
            return withCon.apply(connection);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    public DataSource getDataSource() {
        if (dataSource == null) {
                    dataSource = ApplicationContextHolder.getContext().getBean(DataSource.class);
        }
        return dataSource;
    }
}
