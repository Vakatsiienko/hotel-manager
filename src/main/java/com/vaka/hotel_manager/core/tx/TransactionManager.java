package com.vaka.hotel_manager.core.tx;

import com.vaka.hotel_manager.util.exception.TransactionException;

/**
 * Created by Iaroslav on 12/30/2016.
 */
public interface TransactionManager {

    /**
     * {@link java.sql.Connection#TRANSACTION_NONE}
     */
    int TRANSACTION_NONE             = 0;

    /**
     * {@link java.sql.Connection#TRANSACTION_READ_UNCOMMITTED}
     */
    int TRANSACTION_READ_UNCOMMITTED = 1;

    /**
     * {@link java.sql.Connection#TRANSACTION_READ_COMMITTED}
     */
    int TRANSACTION_READ_COMMITTED   = 2;

    /**
     * {@link java.sql.Connection#TRANSACTION_REPEATABLE_READ}
     */
    int TRANSACTION_REPEATABLE_READ  = 4;

    /**
     * {@link java.sql.Connection#TRANSACTION_SERIALIZABLE}
     */
    int TRANSACTION_SERIALIZABLE     = 8;

    /**
     * @param isolationLevel {@link #TRANSACTION_NONE} <br>
     *                       {@link #TRANSACTION_READ_UNCOMMITTED} <br>
     *                       {@link #TRANSACTION_READ_COMMITTED} <br>
     *                       {@link #TRANSACTION_REPEATABLE_READ} <br>
     *                       {@link #TRANSACTION_SERIALIZABLE}
     * @throws TransactionException
     */
    void begin(int isolationLevel) throws TransactionException;

    default void begin() throws TransactionException {
        begin(getIsolationDefault());
    }

    void commit() throws TransactionException;

    void rollback() throws TransactionException;

    TransactionStatus getStatus();

    void setRollBackOnly(boolean rollbackOnly);

    boolean isRollBackOnly();

    Integer getIsolationDefault();

}
