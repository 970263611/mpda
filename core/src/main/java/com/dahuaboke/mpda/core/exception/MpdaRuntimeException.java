package com.dahuaboke.mpda.core.exception;


/**
 * auth: dahua
 * time: 2025/8/21 14:03
 */
public class MpdaRuntimeException extends RuntimeException {

    public MpdaRuntimeException() {
        super();
    }

    public MpdaRuntimeException(String message) {
        super(message);
    }

    public MpdaRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public MpdaRuntimeException(Throwable cause) {
        super(cause);
    }

    protected MpdaRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
