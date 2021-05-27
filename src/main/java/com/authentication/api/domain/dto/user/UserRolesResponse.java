package com.authentication.api.domain.dto.user;

import com.authentication.api.infrastructure.persistense.entity.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * The User roles response.
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
