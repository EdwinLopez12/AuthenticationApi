package com.authentication.api.infrastructure.controller;

import com.authentication.api.domain.dto.auth.EmailPasswordResetRequest;
import com.authentication.api.domain.dto.auth.PasswordResetRequest;
import com.authentication.api.domain.dto.auth.*;
import com.authentication.api.domain.service.auth.AuthService;
import com.authentication.api.domain.service.auth.RefreshTokenService;
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
    private final RefreshTokenService refreshTokenService;

    /**
     * Signup response entity.
     *
     * @param registerUserRequest the register user request
     * @return the response entity
     */
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody RegisterUserRequest registerUserRequest){
        authService.signup(registerUserRequest);
        return new ResponseEntity<>("User register successfully", HttpStatus.CREATED);
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

    /**
     * Login response entity.
     *
     * @param loginUserRequest the login user request
     * @return the response entity
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody LoginUserRequest loginUserRequest){
        return new ResponseEntity<>(authService.login(loginUserRequest), HttpStatus.OK);
    }

    /**
     * Refresh token response entity.
     *
     * @param refreshTokenRequest the refresh token request
     * @return the response entity
     */
    @PostMapping("/refresh/token")
    public ResponseEntity<AuthenticationResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest){
        return new ResponseEntity<>(authService.refreshToken(refreshTokenRequest), HttpStatus.OK);
    }

    /**
     * Logout response entity.
     *
     * @param logoutUserRequest the logout user request
     * @return the response entity
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody LogoutUserRequest logoutUserRequest){
        refreshTokenService.deleteRefreshToken(logoutUserRequest.getRefreshToken());
        return new ResponseEntity<>("Refresh token deleted successfully", HttpStatus.OK);
    }

    /**
     * Reset password response entity.
     *
     * @param emailPasswordResetRequest the email password reset request
     * @return the response entity
     */
    @PostMapping("/reset/password")
    public ResponseEntity<String> sendEmailResetPassword(@Valid @RequestBody EmailPasswordResetRequest emailPasswordResetRequest){
        authService.sendEmailResetPassword(emailPasswordResetRequest);
        return new ResponseEntity<>("Email was send", HttpStatus.OK);
    }

    /**
     * Update password response entity.
     *
     * @param token the token
     * @return the response entity
     */
    @GetMapping("/reset/password/verification/{token}")
    public ResponseEntity<String> resetPasswordVerifyToken(@PathVariable(name = "token") String token){
        return new ResponseEntity<>(authService.resetPasswordVerifyToken(token), HttpStatus.OK);
    }

    /**
     * Update password response entity.
     *
     * @param passwordResetRequest the password reset request
     * @return the response entity
     */
    @PutMapping("/reset/password")
    public ResponseEntity<String> updatePassword(@Valid @RequestBody PasswordResetRequest passwordResetRequest){
        return new ResponseEntity<>(authService.updatePassword(passwordResetRequest), HttpStatus.OK);
    }
}
