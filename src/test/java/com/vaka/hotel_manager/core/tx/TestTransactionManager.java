package com.vaka.hotel_manager.core.tx;

import com.vaka.hotel_manager.util.exception.TransactionException;

/**
 * Created by Iaroslav on 2/1/2017.
 */
public class TestTransactionManager implements TransactionManager {
    @Override
    public void begin(int isolationLevel) throws TransactionException {

    }

    @Override
    public void commit() throws TransactionException {

    }

    @Override
    public void rollback() throws TransactionException {

    }

    @Override
    public TransactionStatus getStatus() {
        return null;
    }

    @Override
    public void setRollBackOnly(boolean rollbackOnly) {

    }

    @Override
    public boolean isRollBackOnly() {
        return false;
    }

    @Override
    public Integer getIsolationDefault() {
        return null;
    }
}
