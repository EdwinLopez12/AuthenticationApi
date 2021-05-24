package com.authentication.api.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * The Exception model.
 * Used to define a model for exceptions in request fields.
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class ExceptionModel {
    @JsonProperty
    private String fieldName;
    @JsonProperty
    private Object rejectedValue;
    @JsonProperty
    private String errorMessage;
    @JsonProperty
    private String errorCode;
}
