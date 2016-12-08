package com.vaka.hotel_manager.util.exception;

/**
 * Created by Iaroslav on 12/2/2016.
 */
public class CreatingException extends ApplicationException {
    public CreatingException() {
    }

    public CreatingException(String message) {
        super(message);
    }

    public CreatingException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreatingException(Throwable cause) {
        super(cause);
    }

    public CreatingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
