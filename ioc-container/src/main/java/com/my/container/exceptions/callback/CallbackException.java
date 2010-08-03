package com.my.container.exceptions.callback;


public abstract class CallbackException extends RuntimeException {

    public CallbackException(final String message) {
        super(message);
    }

    public CallbackException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public CallbackException(Throwable cause) {
        super(cause);
    }
}

