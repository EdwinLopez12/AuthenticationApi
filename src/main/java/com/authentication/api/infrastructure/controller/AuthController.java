package com.authentication.api.infrastructure.controller;

import com.authentication.api.domain.dto.auth.EmailPasswordResetRequest;
import com.authentication.api.domain.dto.auth.PasswordResetRequest;
import com.authentication.api.domain.dto.auth.*;
import com.authentication.api.domain.service.auth.AuthService;
import com.authentication.api.domain.service.auth.RefreshTokenService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    private final RefreshTokenService refreshTokenService;

    /**
     * Signup response entity.
     *
     * @param registerUserRequest the register user request
     * @return the response entity
     */
    @ApiOperation("Signup user on application")
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
    @ApiOperation("Verify user account by token sent on user email")
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
    @ApiOperation("Login user on the application")
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
    @ApiOperation("Refresh jwt by refresh token")
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
    @ApiOperation("Logout user in the application by deleting refresh token")
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
    @ApiOperation("Send email to reset password")
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
    @ApiOperation("Verify token sent on the user email")
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
    @ApiOperation("Update user password")
    @PutMapping("/reset/password")
    public ResponseEntity<String> updatePassword(@Valid @RequestBody PasswordResetRequest passwordResetRequest){
        return new ResponseEntity<>(authService.updatePassword(passwordResetRequest), HttpStatus.OK);
    }
}
