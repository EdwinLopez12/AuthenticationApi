package com.authentication.api.domain.exception;

/**
 * The Api not found.
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
