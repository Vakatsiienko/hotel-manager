package com.vaka.hotel_manager.core.tx;

import com.vaka.hotel_manager.repository.util.SQLFunction;
import com.vaka.hotel_manager.repository.util.SQLSupplier;
import com.vaka.hotel_manager.util.NullaryFunction;
import com.vaka.hotel_manager.util.exception.TransactionException;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Iaroslav on 12/30/2016.
 */
public class JdbcTransactionManagerImpl implements TransactionManager, ConnectionManager {
    private static final ThreadLocal<JdbcTransaction> LOCAL_TRANSACTION = new ThreadLocal<>();

    private final int isolationDefault;

    private SQLSupplier<Connection> connectionSupplier;

    public JdbcTransactionManagerImpl(SQLSupplier<Connection> connectionSupplier, int isolationDefault) {
        this.connectionSupplier = connectionSupplier;
        this.isolationDefault = isolationDefault;
    }


    private JdbcTransaction createTransaction(int isolationLevel) {
        try {
            JdbcTransaction tx;
            if (LOCAL_TRANSACTION.get() == null) {
                tx = JdbcTransaction.createParent(connectionSupplier.get(), isolationLevel);
                LOCAL_TRANSACTION.set(tx);
            } else tx = JdbcTransaction.createChild(LOCAL_TRANSACTION.get());
            return tx;
        } catch (SQLException e) {
            throw new TransactionException(e);
        }
    }


    public <T> T doTransactional(int isolationLevel, NullaryFunction<T> function) {

        JdbcTransaction tx = createTransaction(isolationLevel);
        try {
            T result;
            try {
                result = function.apply();
            } catch (TransactionException e) {
                tx.rollback();
                throw e;
            }
            tx.commit();
            return result;
        } finally {
            removeTransaction(tx);
        }
    }

    private void removeTransaction(JdbcTransaction tx) {
        if (tx.isParent()) {
            LOCAL_TRANSACTION.remove();
        }
    }

    @Override
    public <T> T withConnection(SQLFunction<Connection, T> withCon) {
        try {
            if (isPresentLocalTransaction()) {
                return LOCAL_TRANSACTION.get().doInTransaction(withCon);
            } else {
                return withOutTransaction(withCon);
            }
        } catch (SQLException e) {
            throw new TransactionException(e);
        }
    }


    private <T> T withOutTransaction(SQLFunction<Connection, T> withCon) throws SQLException {
        try (Connection connection = connectionSupplier.get()) {
            return withCon.apply(connection);
        }
    }

    private boolean isPresentLocalTransaction() {
        return LOCAL_TRANSACTION.get() != null;
    }


    @Override
    public int getIsolationDefault() {
        return isolationDefault;
    }

}
