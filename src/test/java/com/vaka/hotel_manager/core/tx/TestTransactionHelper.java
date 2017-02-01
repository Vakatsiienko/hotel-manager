package com.vaka.hotel_manager.core.tx;

import com.vaka.hotel_manager.core.tx.TransactionHelper;
import com.vaka.hotel_manager.util.NullaryFunction;

/**
 * Created by Iaroslav on 2/1/2017.
 */
public class TestTransactionHelper extends TransactionHelper {
    public TestTransactionHelper(TransactionManager transactionManager) {
        super(transactionManager);
    }

    @Override
    public <T> T doTransactional(int isolationLevel, NullaryFunction<T> function) {
        return function.apply();
    }

    @Override
    public <T> T doTransactional(NullaryFunction<T> function) {
        return function.apply();
    }

    @Override
    public <T> T doInner(NullaryFunction<T> function) {
        return function.apply();
    }
}
