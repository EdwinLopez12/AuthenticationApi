package com.authentication.api.domain.service;

import com.authentication.api.domain.dto.UserRequest;
import com.authentication.api.domain.dto.UserResponse;
import com.authentication.api.infrastructure.persistense.entity.User;
import com.authentication.api.infrastructure.persistense.jpa.UserJpaRepository;
import com.authentication.api.infrastructure.persistense.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The User service.
 */
@Service
@AllArgsConstructor
public class UserService {
    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * Gets data user.
     *
     * @return the data user
     */
    @Transactional
    public UserResponse getDataUser() {
        return userMapper.toDto(getCurrentUser());
    }


    /**
     * Gets current user.
     *
     * @return the current user
     */
    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userJpaRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));
    }

    /**
     * Update user user response.
     *
     * @param userRequest the user request
     * @return the user response
     */
    public UserResponse updateUser(UserRequest userRequest) {
        User user = getCurrentUser();
        if(userRequest.getUsername() != null) user.setUsername(userRequest.getUsername());
        if(userRequest.getEmail() != null) user.setEmail(userRequest.getEmail());
        if(userRequest.getPassword() != null) user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        userJpaRepository.save(user);
        return userMapper.toDto(user);
    }
}
