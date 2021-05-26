package com.authentication.api.domain.service;

import com.authentication.api.domain.dto.user.UserRequest;
import com.authentication.api.domain.dto.user.UserResponse;
import com.authentication.api.domain.dto.user.UserRolesListRequest;
import com.authentication.api.domain.dto.user.UserRolesRequest;
import com.authentication.api.domain.dto.user.UserRolesResponse;
import com.authentication.api.domain.exception.ApiNotFound;
import com.authentication.api.infrastructure.persistense.entity.Role;
import com.authentication.api.infrastructure.persistense.entity.User;
import com.authentication.api.infrastructure.persistense.jpa.RoleJpaRepository;
import com.authentication.api.infrastructure.persistense.jpa.UserJpaRepository;
import com.authentication.api.infrastructure.persistense.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * The User service.
 */
@Service
@AllArgsConstructor
public class UserService {
    private final UserJpaRepository userJpaRepository;
    private final RoleJpaRepository roleJpaRepository;
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
     * @throws UsernameNotFoundException if user can't be found
     */
    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userJpaRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));
    }

    /**
     * Update user.
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

    /**
     * Update user roles.
     *
     * @param id               the id
     * @param userRolesRequest the user roles request
     * @return the user roles response
     */
    public UserRolesResponse updateUserRoles(Long id, UserRolesRequest userRolesRequest) {
        User user = userJpaRepository.findById(id).orElseThrow(()-> new ApiNotFound("User no found"));
        List<Role> roleList = getRoles(userRolesRequest);
        user.setRoles(roleList);
        userJpaRepository.save(user);
        return userMapper.toDtoRolesResponse(user);
    }

    /**
     * Get Role by name
     *
     * @param userRolesRequest the user roles request
     * @return the role list
     */
    private List<Role> getRoles(UserRolesRequest userRolesRequest) {
        List<Role> roleList = new ArrayList<>();
        for (UserRolesListRequest role : userRolesRequest.getRoles()){
            roleList.add(roleJpaRepository.findByName(role.getName()));
        }
        return roleList;
    }
}
