package com.authentication.api.domain.dto.user;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.Valid;
import java.util.List;

/**
 * The User roles request.
 * Used to provide specific data to update user role.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRolesRequest {
    private List<@Valid UserRolesListRequest> roles;
}
