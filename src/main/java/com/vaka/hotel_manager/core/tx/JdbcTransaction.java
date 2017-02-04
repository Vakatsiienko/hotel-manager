package com.vaka.hotel_manager.core.tx;

import com.vaka.hotel_manager.repository.util.SQLFunction;
import com.vaka.hotel_manager.util.exception.TransactionException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Created by Iaroslav on 2/2/2017.
 */
public class JdbcTransaction {
    private final Connection connection;
    private final Optional<JdbcTransaction> parent;

    private JdbcTransaction(Connection connection) {
        this.connection = connection;
        parent = Optional.empty();
    }

    private JdbcTransaction(JdbcTransaction parent) {
        this.connection = parent.connection;
        this.parent = Optional.of(parent);
    }

    public static JdbcTransaction createParent(Connection connection, int isolationLevel) {
        try {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(isolationLevel);
            return new JdbcTransaction(connection);
        } catch (SQLException e) {
            throw new TransactionException(e);
        }
    }

    public static JdbcTransaction createChild(JdbcTransaction parent) {
        return new JdbcTransaction(parent);
    }

    public <T> T doInTransaction(SQLFunction<Connection, T> function) {
        try {
            return function.apply(connection);
        } catch (SQLException e) {
            throw new TransactionException(e);
        }
    }


    public void commit() {
        if (!parent.isPresent()) {
            try (Connection con = this.connection) {
                con.commit();
            } catch (SQLException e) {
                throw new TransactionException(e);
            }
        }
    }

    public void rollback() {
        try (Connection con = this.connection) {
            con.rollback();
        } catch (SQLException e) {
            throw new TransactionException(e);
        }
    }

    public boolean isParent() {
        return !parent.isPresent();
    }

}
