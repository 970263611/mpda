package com.dahuaboke.mpda.core.exception;


/**
 * auth: dahua
 * time: 2025/8/22 11:53
 */
public class MpdaIllegalArgumentException extends MpdaRuntimeException {

    public MpdaIllegalArgumentException() {
        super();
    }

    public MpdaIllegalArgumentException(String message) {
        super(message);
    }

    public MpdaIllegalArgumentException(Throwable cause) {
        super(cause);
    }

    public MpdaIllegalArgumentException(String message, Throwable cause) {
        super(message, cause);
    }
}
