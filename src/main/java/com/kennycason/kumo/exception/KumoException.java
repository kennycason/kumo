package com.kennycason.kumo.exception;

/**
 * Created by kenny on 2/20/16.
 */
public class KumoException extends RuntimeException {
    public KumoException(Throwable cause) {
        super(cause);
    }

    public KumoException(String message, Throwable cause) {
        super(message, cause);
    }
}

