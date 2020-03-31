package org.example.userservice.common.exception;

import lombok.Getter;

/**
 * Wrapper for exceptions happening in service class level.
 */
@Getter
public abstract class ServiceException extends RuntimeException {

    /**
     * HTTP status corresponding to the exception.
     */
    protected Integer httpStatus;

    public ServiceException(final String s) {
        super(s);
    }
}
