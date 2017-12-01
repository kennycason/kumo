package com.kennycason.kumo.exception;

/**
 * Created by kenny on 2/20/16.
 */
public class KumoException extends RuntimeException {

    public KumoException(final String message) {
        super(message);
    }

    public KumoException(final Throwable cause) {
        super(cause);
    }

    public KumoException(final String message, final Throwable cause) {
        super(message, cause);
    }

}

