package com.authentication.api.infrastructure.persistence.mapper;

import com.authentication.api.domain.dto.user.UserResponse;
import com.authentication.api.domain.dto.auth.PasswordResetRequest;
import com.authentication.api.domain.dto.user.UserRolesResponse;
import com.authentication.api.infrastructure.persistence.entity.Role;
import com.authentication.api.infrastructure.persistence.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * The User mapper.
 */
@Component
@AllArgsConstructor
public class UserMapper {

    /**
     * To dto user response.
     *
     * @param user the user
     * @return the user response
     */
    public UserResponse toDto(User user){
        return UserResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    /**
     * To dto user roles response.
     *
     * @param user the user
     * @return the user roles response
     */
    public UserRolesResponse toDtoRolesResponse(User user) {
        return UserRolesResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles())
                .build();
    }

    /**
     * To entity user.
     *
     * @param id                   the id
     * @param username             the username
     * @param isEnable             the is enable
     * @param roles                the roles
     * @param passwordResetRequest the password reset request
     * @return the user
     */
    public User toEntity(Long id, String username, Boolean isEnable, List<Role> roles, PasswordResetRequest passwordResetRequest) {
        return User.builder()
                .id(id)
                .username(username)
                .email(passwordResetRequest.getEmail())
                .isEnable(isEnable)
                .roles(roles)
                .password(passwordResetRequest.getPassword())
                .build();
    }


}
