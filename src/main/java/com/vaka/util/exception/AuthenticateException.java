package com.vaka.util.exception;

/**
 * Created by Iaroslav on 11/26/2016.
 */
public class AuthenticateException extends RuntimeException {
    public AuthenticateException() {
    }

    public AuthenticateException(String message) {
        super(message);
    }

    public AuthenticateException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticateException(Throwable cause) {
        super(cause);
    }

    public AuthenticateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
