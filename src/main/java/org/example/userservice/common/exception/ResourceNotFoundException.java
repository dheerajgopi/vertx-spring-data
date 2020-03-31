package org.example.userservice.common.exception;

import lombok.Getter;

/**
 * Exception for 404.
 */
@Getter
public class ResourceNotFoundException extends ServiceException {

    public ResourceNotFoundException(final String message) {
        super(message);
        this.httpStatus = 404;
    }

}
