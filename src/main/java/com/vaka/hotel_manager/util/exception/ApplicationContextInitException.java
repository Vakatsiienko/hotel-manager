package com.vaka.hotel_manager.util.exception;

/**
 * Created by Iaroslav on 11/26/2016.
 */
public class ApplicationContextInitException extends RuntimeException {
    public ApplicationContextInitException(Throwable cause) {
        super(cause);
    }

    public ApplicationContextInitException() {
    }

    public ApplicationContextInitException(String message) {
        super(message);
    }

    public ApplicationContextInitException(String message, Throwable cause) {
        super(message, cause);
    }
}
