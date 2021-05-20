package com.authentication.api.domain.dto;

import com.authentication.api.domain.model.ExceptionModel;
import com.authentication.api.domain.utils.FormatDates;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Exception handler response.
 */
@JsonPropertyOrder({
        "timestamp",
        "status",
        "code",
        "error",
        "message",
        "errors",
        "path"
})

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExceptionHandlerResponse {
    @JsonProperty
    @Builder.Default
    private String timestamp = FormatDates.instantToString(Instant.now());
    @JsonProperty
    @Builder.Default
    private String status = "Error";
    @JsonProperty
    private Integer code;
    @JsonProperty
    private String message;
    @JsonProperty
    private List<ExceptionModel> errors = new ArrayList<>();
    @JsonProperty
    private String path;
}
