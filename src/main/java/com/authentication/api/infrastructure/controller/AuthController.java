package com.authentication.api.infrastructure.controller;

import com.authentication.api.domain.dto.auth.AuthenticationResponse;
import com.authentication.api.domain.dto.auth.LoginUserRequest;
import com.authentication.api.domain.dto.auth.RegisterUserRequest;
import com.authentication.api.domain.service.auth.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /**
     * Verify account response entity.
     *
     * @param token the token
     * @return the response entity
     */
    @GetMapping("/account-verification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable(name = "token") String token){
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account activated successfully", HttpStatus.OK);
    }

    @PostMapping("login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody LoginUserRequest loginUserRequest){
        return new ResponseEntity<>(authService.login(loginUserRequest), HttpStatus.OK);
    }
}
