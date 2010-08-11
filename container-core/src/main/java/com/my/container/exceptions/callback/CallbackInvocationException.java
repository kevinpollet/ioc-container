package com.my.container.exceptions.callback;


public class CallbackInvocationException extends CallbackException {

    public CallbackInvocationException(final String message) {
        super(message);
    }

    public CallbackInvocationException(String message, Throwable cause) {
        super(message, cause);
    }

    public CallbackInvocationException(Throwable cause) {
        super(cause);
    }
}
