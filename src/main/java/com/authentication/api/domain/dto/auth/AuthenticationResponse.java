package com.authentication.api.domain.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * The Authentication response.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
