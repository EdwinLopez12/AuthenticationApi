package com.authentication.api.domain.dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * The Logout user request.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class LogoutUserRequest {
    @JsonProperty
    @NotBlank(message = "Refresh token is required")
    private String refreshToken;
}
