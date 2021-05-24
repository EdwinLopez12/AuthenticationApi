package com.authentication.api.domain.dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * The Password reset request.
 * Used to provide the specific data to reset password (Edit password).
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PasswordResetRequest {
    @JsonProperty
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be in the correct format")
    private String email;

    @JsonProperty
    @NotBlank(message = "Password is required")
    @Length(min = 6, message = "Password should be greater than or equal to 6")
    private String password;

    @JsonProperty
    @NotBlank(message = "Password verify is required")
    @Length(min = 6, message = "Password verify should be greater than or equal to 6")
    private String passwordVerify;
}
