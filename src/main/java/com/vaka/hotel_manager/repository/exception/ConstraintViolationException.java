package com.vaka.hotel_manager.repository.exception;

import com.vaka.hotel_manager.util.exception.RepositoryException;

/**
 * Created by Iaroslav on 2/2/2017.
 */
public class ConstraintViolationException extends RepositoryException {
    public ConstraintViolationException() {
    }

    public ConstraintViolationException(String message) {
        super(message);
    }

    public ConstraintViolationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConstraintViolationException(Throwable cause) {
        super(cause);
    }

    public ConstraintViolationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
