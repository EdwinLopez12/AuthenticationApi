package com.authentication.api.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;

/**
 * The User request.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRequest {
    @JsonProperty
    @Length(min = 3, max = 255, message = "User name should be greater than or equal to 3")
    private String username;
    @JsonProperty
    @Email(message = "Email must be in the correct format")
    private String email;
    @JsonProperty
    @Length(min = 6, message = "Password should be greater than or equal to 6")
    private String password;
}
