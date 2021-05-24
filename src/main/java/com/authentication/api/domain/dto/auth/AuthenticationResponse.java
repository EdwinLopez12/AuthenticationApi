package com.authentication.api.domain.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * The Authentication response.
 * Used to return the necessary data to login, refresh token and logout.
 */
@JsonPropertyOrder({
        "authenticationToken",
        "refreshToken",
        "expiresAt",
        "username"
})

@Getter
@Setter
@AllArgsConstructor
@Builder
public class AuthenticationResponse {
    @JsonProperty
    private String authenticationToken;
    @JsonProperty
    private String refreshToken;
    @JsonProperty
    private String expiresAt;
    @JsonProperty
    private String username;
}
