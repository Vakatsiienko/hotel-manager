package com.vaka.hotel_manager.core.tx;

/**
 * Created by Iaroslav on 1/14/2017.
 */
public enum TransactionStatus {
    /**
     * The transaction has not yet been begun
     */
    NOT_ACTIVE,
    /**
     * The transaction has been begun, but not yet completed.
     */
    ACTIVE,
    /**
     * Same as active, but all methods(e.g. commit, begin) except rollback are disabled.
     */
    ACTIVE_ROLLBACK_ONLY,
}
