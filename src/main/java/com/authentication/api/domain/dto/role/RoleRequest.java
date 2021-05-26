package com.authentication.api.domain.dto.role;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * The Role request.
 * Used to provide specific data to create Role.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleRequest {
    @JsonProperty
    @NotBlank(message = "Role name is required")
    @Length(min = 3, max = 255, message = "Role name should be greater than or equal to 3")
    private String name;
    private List<@Valid PrivilegeRequest> privileges;
}
