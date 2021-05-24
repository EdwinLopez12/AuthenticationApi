package com.authentication.api.domain.exception;

/**
 * The Api conflict exception.
 * Used to throw an exception when data duplicated
 */
public class ApiConflict extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new Api conflict.
     *
     * @param message the message
     */
    public ApiConflict(String message){
        super(message);
    }
}
