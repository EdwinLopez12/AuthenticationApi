package com.authentication.api.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * The User response.
 * Used to return user data.
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserResponse {
    @JsonProperty
    private String username;
    @JsonProperty
    private String email;
}
