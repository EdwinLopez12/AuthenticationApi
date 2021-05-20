package com.authentication.api.domain.dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * The Register user request.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegisterUserRequest {

    @JsonProperty
    @NotBlank(message = "Username is required")
    @Length(min = 3, max = 255, message = "User name should be greater than or equal to 3")
    private String username;

    @JsonProperty
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be in the correct format")
    private String email;

    @JsonProperty
    @NotBlank(message = "Password is required")
    @Length(min = 6, message = "Password should be greater than or equal to 6")
    private String password;
}
