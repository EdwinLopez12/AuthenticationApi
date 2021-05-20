package com.authentication.api.domain.exception;

import org.springframework.mail.MailException;

/**
 * The Authentication api exception.
 */
public class AuthenticationApiException extends RuntimeException{
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
