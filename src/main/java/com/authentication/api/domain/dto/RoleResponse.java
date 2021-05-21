package com.authentication.api.domain.dto;

import com.authentication.api.infrastructure.persistense.entity.Privilege;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/**
 * The Role response.
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
