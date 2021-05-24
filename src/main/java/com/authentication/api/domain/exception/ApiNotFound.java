package com.authentication.api.domain.exception;

/**
 * The Api not found exception.
 * Used to throw an exception when no data is found or the URL doesn't exist
 */
public class ApiNotFound extends RuntimeException{
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new Api not found.
     *
     * @param message the message
     */
    public ApiNotFound(String message){
        super(message);
    }
}
