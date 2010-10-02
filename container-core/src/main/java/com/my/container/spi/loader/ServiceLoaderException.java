package com.my.container.spi.loader;

/**
 * This exception was thrown when service
 * loader encounter a problem during loading.
 *
 * @author kevinpollet
 */
public class ServiceLoaderException extends RuntimeException {

    /**
     * {@inheritDoc}
     */
    public ServiceLoaderException(final String message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    public ServiceLoaderException(final String message,final Throwable cause) {
        super(message, cause);
    }

    /**
     * {@inheritDoc}
     */
    public ServiceLoaderException(final Throwable cause) {
        super(cause);
    }
}
