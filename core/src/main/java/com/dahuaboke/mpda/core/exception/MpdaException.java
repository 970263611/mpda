package com.dahuaboke.mpda.core.exception;


/**
 * auth: dahua
 * time: 2025/8/21 14:03
 */
public class MpdaException extends Exception {

    public MpdaException() {
        super();
    }

    public MpdaException(String message) {
        super(message);
    }

    public MpdaException(String message, Throwable cause) {
        super(message, cause);
    }

    public MpdaException(Throwable cause) {
        super(cause);
    }

    protected MpdaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
