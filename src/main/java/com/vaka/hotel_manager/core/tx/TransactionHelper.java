package com.vaka.hotel_manager.core.tx;

import com.vaka.hotel_manager.core.context.ApplicationContext;
import com.vaka.hotel_manager.util.NullaryFunction;

/**
 * Created by Iaroslav on 1/15/2017.
 */
public class TransactionHelper {
    public TransactionHelper(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    private TransactionManager transactionManager;

    public <T> T doTransactional(int isolationLevel, NullaryFunction<T> function) {
        transactionManager.begin(isolationLevel);
        try {
            T result = function.apply();
            if (transactionManager.getStatus() == TransactionStatus.ACTIVE)
                transactionManager.commit();
            return result;
        } finally {
            if (transactionManager.getStatus() == TransactionStatus.ACTIVE)
                transactionManager.rollback();
        }
    }

    public <T> T doTransactional(NullaryFunction<T> function) {
        return doTransactional(transactionManager.getIsolationDefault(), function);
    }

    public <T> T doInner(NullaryFunction<T> function) {
        boolean rollbackOnly = transactionManager.isRollBackOnly();
        try {
            transactionManager.setRollBackOnly(true);
            return function.apply();
        } finally {
            transactionManager.setRollBackOnly(rollbackOnly);
        }
    }

}
