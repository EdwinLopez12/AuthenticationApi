package com.authentication.api.infrastructure.controller;

import com.authentication.api.domain.dto.auth.RegisterUserRequest;
import com.authentication.api.domain.service.auth.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * The Auth controller.
 */
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Signup response entity.
     *
     * @param registerUserRequest the register user request
     * @return the response entity
     */
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody RegisterUserRequest registerUserRequest){
        authService.signup(registerUserRequest);
        return new ResponseEntity<>("User register successfully", HttpStatus.OK);
    }
}
