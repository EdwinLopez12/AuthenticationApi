package com.authentication.api.domain.dto.role;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * The Privilege request.
 * Used to provide specific data to Role (create new role and update role needs a list of privileges).
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PrivilegeRequest {
    @JsonProperty
    @NotBlank(message = "Privilege is required")
    private String name;
}
