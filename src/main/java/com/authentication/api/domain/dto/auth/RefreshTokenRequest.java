package com.authentication.api.domain.dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * The Refresh token request.
 * Used to provide the specific data to refresh jwt.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class RefreshTokenRequest {
    @JsonProperty
    @NotBlank(message = "Refresh Token is required")
    private String refreshToken;
    @JsonProperty
    @NotBlank(message = "Username is required")
    private String username;
}
