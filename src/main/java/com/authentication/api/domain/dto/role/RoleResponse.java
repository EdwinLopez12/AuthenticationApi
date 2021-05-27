package com.authentication.api.domain.dto.role;

import com.authentication.api.infrastructure.persistense.entity.Privilege;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * The Role response.
 * Used to return the role data.
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class RoleResponse {
    @JsonProperty
    private String name;
    @JsonProperty
    private List<Privilege> privileges;
}
