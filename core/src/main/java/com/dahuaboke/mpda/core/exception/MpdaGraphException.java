package com.dahuaboke.mpda.core.exception;


/**
 * auth: dahua
 * time: 2025/8/21 14:17
 */
public class MpdaGraphException extends MpdaException {

    public MpdaGraphException(String message) {
        super(message);
    }

    public MpdaGraphException() {
        super();
    }

    public MpdaGraphException(Throwable cause) {
        super(cause);
    }

    public MpdaGraphException(String message, Throwable cause) {
        super(message, cause);
    }
}
