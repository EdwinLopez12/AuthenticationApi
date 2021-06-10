package com.authentication.api.domain.dto.user;

import com.authentication.api.infrastructure.persistence.entity.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * The User roles response.
 * Used to return the user role data
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRolesResponse {
    @JsonProperty
    private String username;
    @JsonProperty
    private String email;
    private List<Role> roles;
}
