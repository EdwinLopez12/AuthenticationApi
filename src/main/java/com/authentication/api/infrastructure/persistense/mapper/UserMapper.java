package com.authentication.api.infrastructure.persistense.mapper;

import com.authentication.api.domain.dto.UserResponse;
import com.authentication.api.infrastructure.persistense.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

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

}
