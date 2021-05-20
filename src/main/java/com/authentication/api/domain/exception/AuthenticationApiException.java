package com.authentication.api.domain.exception;

import org.springframework.mail.MailException;

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

    public AuthenticationApiException(String message, Exception e) {
        super(message, e);
    }
}
