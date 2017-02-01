package com.vaka.hotel_manager.util.exception;

/**
 * Created by Iaroslav on 2/1/2017.
 */
public class EmailExistException extends RuntimeException {
    public EmailExistException() {
        super();
    }

    public EmailExistException(String message) {
        super(message);
    }

    public EmailExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailExistException(Throwable cause) {
        super(cause);
    }

    protected EmailExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
