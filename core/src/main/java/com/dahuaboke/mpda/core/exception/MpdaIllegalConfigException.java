package com.dahuaboke.mpda.core.exception;


/**
 * auth: dahua
 * time: 2025/8/22 11:53
 */
public class MpdaIllegalConfigException extends MpdaRuntimeException {

    public MpdaIllegalConfigException() {
        super();
    }

    public MpdaIllegalConfigException(String message) {
        super(message);
    }

    public MpdaIllegalConfigException(Throwable cause) {
        super(cause);
    }

    public MpdaIllegalConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
