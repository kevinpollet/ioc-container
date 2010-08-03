package com.my.container.exceptions.callback;

public class CallbackDefinitionException extends CallbackException {

    public CallbackDefinitionException(final String message) {
        super(message);
    }

    public CallbackDefinitionException(String message, Throwable cause) {
        super(message, cause);
    }

    public CallbackDefinitionException(Throwable cause) {
        super(cause);
    }
}
