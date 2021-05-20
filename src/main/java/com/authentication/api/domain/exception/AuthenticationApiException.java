package com.authentication.api.domain.exception;

/**
 * The Authentication api exception.
 */
public class AuthenticationApiException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new Authentication api exception.
     *
     * @param message the message
     */
    public AuthenticationApiException(String message){
        super(message);
    }

    /**
     * Instantiates a new Authentication api exception.
     *
     * @param message the message
     * @param e       the e
     */
    public AuthenticationApiException(String message, Exception e) {
        super(message, e);
    }
}
