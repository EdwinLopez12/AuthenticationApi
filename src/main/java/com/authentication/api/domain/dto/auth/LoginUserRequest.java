package com.authentication.api.domain.dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * The Login user request.
 * Used to provide the specific data to login user.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginUserRequest {
    @JsonProperty
    @NotBlank(message = "username name is required")
    @Length(min = 3, message = "Username should be greater than or equal to 3")
    private String username;

    @JsonProperty
    @NotBlank(message = "Password is required")
    @Length(min = 6, message = "Password should be greater than or equal to 6")
    private String password;
}
