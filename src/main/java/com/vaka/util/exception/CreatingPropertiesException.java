package com.vaka.util.exception;

/**
 * Created by Iaroslav on 12/3/2016.
 */
public class CreatingPropertiesException extends RuntimeException {
    public CreatingPropertiesException() {
    }

    public CreatingPropertiesException(String message) {
        super(message);
    }

    public CreatingPropertiesException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreatingPropertiesException(Throwable cause) {
        super(cause);
    }

    public CreatingPropertiesException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
