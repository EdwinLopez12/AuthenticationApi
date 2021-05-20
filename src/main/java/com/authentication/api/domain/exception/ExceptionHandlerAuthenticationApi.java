package com.authentication.api.domain.exception;

import com.authentication.api.domain.dto.ExceptionHandlerResponse;
import com.authentication.api.domain.model.ExceptionModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * The type Handler authentication api exception.
 */
@RestControllerAdvice
public class ExceptionHandlerAuthenticationApi {
    /**
     * Handle authentication exception exception handler response.
     *
     * @param request   the request
     * @param exception the exception
     * @return the exception handler response
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionHandlerResponse handleAuthenticationException(HttpServletRequest request, Throwable exception){
        return ExceptionHandlerResponse.builder()
                .code(HttpStatus.UNAUTHORIZED.value())
                .message(exception.getLocalizedMessage())
                .path(request.getServletPath())
                .build();
    }

    /**
     * Handle access denied exception exception handler response.
     *
     * @param request the request
     * @return the exception handler response
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionHandlerResponse handleAccessDeniedException(HttpServletRequest request) {
        return ExceptionHandlerResponse.builder()
                .code(HttpStatus.UNAUTHORIZED.value())
                .message(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .path(request.getServletPath())
                .build();
    }

    /**
     * Handle global exception exception handler response.
     *
     * @param request   the request
     * @param exception the exception
     * @return the exception handler response
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionHandlerResponse handleGlobalException(HttpServletRequest request, Throwable exception) {
        return ExceptionHandlerResponse.builder()
                .status("Error -")
                .message(exception.getLocalizedMessage())
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .path(request.getServletPath())
                .build();
    }

    /**
     * Handle conflict exception exception handler response.
     *
     * @param request   the request
     * @param exception the exception
     * @return the exception handler response
     */
    @ExceptionHandler(ApiConflict.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionHandlerResponse handleConflictException(HttpServletRequest request, Throwable exception){
        return ExceptionHandlerResponse.builder()
                .code(HttpStatus.CONFLICT.value())
                .message(exception.getLocalizedMessage())
                .path(request.getServletPath())
                .build();
    }

    /**
     * Handle not found exception exception handler response.
     *
     * @param request   the request
     * @param exception the exception
     * @return the exception handler response
     */
    @ExceptionHandler(ApiNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionHandlerResponse handleNotFoundException(HttpServletRequest request, Throwable exception){
        return ExceptionHandlerResponse.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .message(exception.getLocalizedMessage())
                .path(request.getServletPath())
                .build();
    }

    /**
     * Handle unprocessable entity exception handler response.
     *
     * @param request   the request
     * @param exception the exception
     * @return the exception handler response
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ExceptionHandlerResponse handleUnprocessableEntity(HttpServletRequest request, MethodArgumentNotValidException exception){
        ExceptionHandlerResponse exceptionHandlerResponse = new ExceptionHandlerResponse();
        for (ObjectError error : exception.getBindingResult().getAllErrors()) {
            ExceptionModel errorModel = ExceptionModel.builder()
                    .fieldName(((FieldError) error).getField())
                    .rejectedValue(((FieldError) error).getRejectedValue())
                    .errorMessage(error.getDefaultMessage())
                    .errorCode(error.getCode())
                    .build();
            exceptionHandlerResponse.getErrors().add(errorModel);
        }
        exceptionHandlerResponse.setCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
        exceptionHandlerResponse.setMessage("Validation fails");
        exceptionHandlerResponse.setPath(request.getServletPath());
        return exceptionHandlerResponse;
    }

}
