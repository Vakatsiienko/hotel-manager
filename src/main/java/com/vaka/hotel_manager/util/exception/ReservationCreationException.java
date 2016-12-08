package com.vaka.hotel_manager.util.exception;

/**
 * Created by Iaroslav on 12/1/2016.
 */
public class ReservationCreationException extends ApplicationException {
    public ReservationCreationException() {
    }

    public ReservationCreationException(String message) {
        super(message);
    }

    public ReservationCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReservationCreationException(Throwable cause) {
        super(cause);
    }

    public ReservationCreationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
