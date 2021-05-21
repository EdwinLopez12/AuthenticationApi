package com.authentication.api.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * The Exception model.
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
