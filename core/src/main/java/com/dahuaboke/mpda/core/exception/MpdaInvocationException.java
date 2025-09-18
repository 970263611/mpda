package com.dahuaboke.mpda.core.exception;


/**
 * auth: dahua
 * time: 2025/8/21 15:30
 */
public class MpdaInvocationException extends MpdaRuntimeException {

    public MpdaInvocationException() {
        super();
    }

    public MpdaInvocationException(String message) {
        super(message);
    }

    public MpdaInvocationException(Throwable cause) {
        super(cause);
    }
}
