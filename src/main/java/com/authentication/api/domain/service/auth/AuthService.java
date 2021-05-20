package com.authentication.api.domain.service.auth;

import com.authentication.api.domain.dto.auth.AuthenticationResponse;
import com.authentication.api.domain.dto.auth.LoginUserRequest;
import com.authentication.api.domain.dto.auth.RefreshTokenRequest;
import com.authentication.api.domain.dto.auth.RegisterUserRequest;
import com.authentication.api.domain.exception.ApiConflict;
import com.authentication.api.domain.exception.AuthenticationApiException;
import com.authentication.api.domain.service.mail.MailService;
import com.authentication.api.domain.utils.FormatDates;
import com.authentication.api.infrastructure.persistense.entity.User;
import com.authentication.api.infrastructure.persistense.entity.VerificationToken;
import com.authentication.api.infrastructure.persistense.jpa.RoleJpaRepository;
import com.authentication.api.infrastructure.persistense.jpa.UserJpaRepository;
import com.authentication.api.infrastructure.persistense.jpa.VerificationTokenJpaRepository;
import com.authentication.api.infrastructure.security.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

/**
 * The type Auth service.
 */
@Service
@AllArgsConstructor
public class AuthService {

    private final UserJpaRepository userJpaRepository;
    private final RoleJpaRepository roleJpaRepository;
    private final VerificationTokenJpaRepository verificationTokenJpaRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final MailService mailService;
    private final RefreshTokenService refreshTokenService;

    /**
     * Signup.
     *
     * @param registerUserRequest the register user request
     */
    @Transactional
    public void signup(RegisterUserRequest registerUserRequest) {
        Optional<User> usernameExist = userJpaRepository.findByUsername(registerUserRequest.getUsername());
        Optional<User> emailExist = userJpaRepository.findByEmail(registerUserRequest.getEmail());
        if(!usernameExist.isPresent()){
            if(!emailExist.isPresent()){
                User user = User.builder()
                        .username(registerUserRequest.getUsername())
                        .email(registerUserRequest.getEmail())
                        .password(passwordEncoder.encode(registerUserRequest.getPassword()))
                        .isEnable(false)
                        .roles(Collections.singletonList(roleJpaRepository.findByName("USER")))
                        .build();
                String token = generateVerificationToken(user);
                userJpaRepository.save(user);
                mailService.setUpEmailData(user.getEmail(), token);
            }else{
                throw new ApiConflict("Email already exist");
            }
        }else{
            throw new ApiConflict("Username already exist");
        }
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .user(user)
                .build();
        verificationTokenJpaRepository.save(verificationToken);
        return token;
    }

    /**
     * Verify account.
     *
     * @param token the token
     */
    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenJpaRepository.findByToken(token);
        fetchUserAndEnable(verificationToken.orElseThrow(() -> new AuthenticationApiException("Invalid Token")));
    }

    /**
     * Fetch user and enable.
     *
     * @param verificationToken the verification token
     */
    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user = userJpaRepository.findByUsername(username).orElseThrow(() -> new AuthenticationApiException("User not found with name - " + username));
        user.setIsEnable(true);
        userJpaRepository.save(user);
    }

    /**
     * Login authentication response.
     *
     * @param loginUserRequest the login user request
     * @return the authentication response
     */
    public AuthenticationResponse login(LoginUserRequest loginUserRequest) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUserRequest.getUsername(),
                        loginUserRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(FormatDates.instantToString(Instant.now().plusMillis(jwtProvider.getJwtExpirationMillis())))
                .username(loginUserRequest.getUsername())
                .build();
    }

    /**
     * Refresh token authentication response.
     *
     * @param refreshTokenRequest the refresh token request
     * @return the authentication response
     */
    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        User user = userJpaRepository.findByUsername(refreshTokenRequest.getUsername()).orElseThrow(()-> new UsernameNotFoundException("User no found with username: "+refreshTokenRequest.getUsername()));
        String token = jwtProvider.generateTokenWithUsername(user.getUsername());
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(FormatDates.instantToString(Instant.now().plusMillis(jwtProvider.getJwtExpirationMillis())))
                .username(user.getUsername())
                .build();
    }
}
