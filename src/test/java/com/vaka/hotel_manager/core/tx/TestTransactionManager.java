package com.vaka.hotel_manager.core.tx;

import com.vaka.hotel_manager.util.NullaryFunction;

/**
 * Created by Iaroslav on 2/1/2017.
 */
public class TestTransactionManager implements TransactionManager {
    @Override
    public <T> T doTransactional(int isolationLevel, NullaryFunction<T> function) {
        return function.apply();
    }

    @Override
    public int getIsolationDefault() {
        return 0;
    }
}
