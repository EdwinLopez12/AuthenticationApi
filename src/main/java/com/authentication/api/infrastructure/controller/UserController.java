package com.authentication.api.infrastructure.controller;

import com.authentication.api.domain.dto.user.UserRequest;
import com.authentication.api.domain.dto.user.UserResponse;
import com.authentication.api.domain.dto.user.UserRolesRequest;
import com.authentication.api.domain.dto.user.UserRolesResponse;
import com.authentication.api.domain.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * The User controller.
 */
@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Get data user response entity.
     *
     * @return the response entity
     */
    @ApiOperation("Get user data")
    @PreAuthorize("hasAuthority('READ_USER')")
    @GetMapping("/account")
    public ResponseEntity<UserResponse> getDataUser(){
        return new ResponseEntity<>(userService.getDataUser(), HttpStatus.OK);
    }

    /**
     * Update user response entity.
     *
     * @param userRequest the user request
     * @return the response entity
     */
    @ApiOperation("Edit user data")
    @PreAuthorize("hasAuthority('EDIT_USER')")
    @PutMapping("/account")
    public ResponseEntity<UserResponse> updateUser(@Valid @RequestBody UserRequest userRequest){
        return new ResponseEntity<>(userService.updateUser(userRequest), HttpStatus.OK);
    }

    /**
     * Update user roles response entity.
     *
     * @param id               the id
     * @param userRolesRequest the user roles request
     * @return the response entity
     */
    @ApiOperation("Edit user roles")
    @PreAuthorize("hasAuthority('EDIT_USER')")
    @PutMapping("/roles/{id}")
    public ResponseEntity<UserRolesResponse> updateUserRoles(@Valid @PathVariable(name = "id") Long id, @RequestBody UserRolesRequest userRolesRequest){
        return new ResponseEntity<>(userService.updateUserRoles(id, userRolesRequest), HttpStatus.OK);
    }
}
