package com.authentication.api.domain.service.auth;

import com.authentication.api.domain.dto.auth.*;
import com.authentication.api.domain.exception.ApiConflict;
import com.authentication.api.domain.exception.ApiNotFound;
import com.authentication.api.domain.exception.AuthenticationApiException;
import com.authentication.api.domain.service.mail.MailService;
import com.authentication.api.domain.utils.FormatDates;
import com.authentication.api.infrastructure.persistence.entity.PasswordReset;
import com.authentication.api.infrastructure.persistence.entity.User;
import com.authentication.api.infrastructure.persistence.entity.VerificationToken;
import com.authentication.api.infrastructure.persistence.jpa.PasswordResetJpaRepository;
import com.authentication.api.infrastructure.persistence.jpa.RoleJpaRepository;
import com.authentication.api.infrastructure.persistence.jpa.UserJpaRepository;
import com.authentication.api.infrastructure.persistence.jpa.VerificationTokenJpaRepository;
import com.authentication.api.infrastructure.persistence.mapper.UserMapper;
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
    private final PasswordResetJpaRepository passwordResetJpaRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final MailService mailService;
    private final RefreshTokenService refreshTokenService;
    private final UserMapper userMapper;

    private static final String SUBJECT ="Authentication API account";
    private static final String TITLE = "Hello! ";
    private static final String BODY_SIGNUP = "Thanks to join us. Please click on the below url to active your account";
    private static final String BODY_RESET_PASSWORD = "You are receiving this email because we received a password reset request from your account. If you did not request a password reset, no further action required, but if you did, please click on the below url to reset your password ";
    private static final String END_POINT_SIGNUP = "account-verification";
    private static final String END_POINT_RESET_PASSWORD = "reset/password";

    /**
     * Signup the user.
     *
     * @param registerUserRequest the register user request
     * @throws ApiConflict if email already exist
     * @throws ApiConflict if username already exist
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
                        .roles(Collections.singletonList(roleJpaRepository.findByName("ADMIN_ROLE")))
                        .build();
                userJpaRepository.save(user);
                String token = generateVerificationToken(user);
                mailService.setUpEmailData(SUBJECT, TITLE, user.getEmail(), BODY_SIGNUP, END_POINT_SIGNUP, token);
            }else{
                throw new ApiConflict("Email already exist");
            }
        }else{
            throw new ApiConflict("Username already exist");
        }
    }

    /**
     * Generate the verification token for the user
     *
     * @param user the user model
     * @return token
     */
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
     * Verify account by token.
     *
     * @param token the token
     * @throws AuthenticationApiException if token is invalid
     */
    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenJpaRepository.findByToken(token);
        fetchUserAndEnable(verificationToken.orElseThrow(() -> new AuthenticationApiException("Invalid Token")));
    }

    /**
     * Fetch user and enable.
     *
     * @throws AuthenticationApiException if user can't be found
     * @param verificationToken the verification token
     */
    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user = userJpaRepository.findByUsername(username).orElseThrow(() -> new AuthenticationApiException("User not found with name - " + username));
        user.setIsEnable(true);
        userJpaRepository.save(user);
    }

    /**
     * Login the user.
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
     * Refresh jwt token .
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

    /**
     * Send email with token to reset password.
     *
     * @throws ApiNotFound if email doesn't exist or can't be found
     * @param emailPasswordResetRequest the email password reset request
     */
    public void sendEmailResetPassword(EmailPasswordResetRequest emailPasswordResetRequest) {
        Optional<User> user = userJpaRepository.findByEmail(emailPasswordResetRequest.getEmail());
        if(user.isPresent()){
            String token = UUID.randomUUID().toString();
            PasswordReset passwordReset = PasswordReset.builder()
                    .email(user.get().getEmail())
                    .token(token)
                    .date(Instant.now())
                    .build();
            passwordResetJpaRepository.save(passwordReset);
            mailService.setUpEmailData(SUBJECT,  TITLE, user.get().getEmail(), BODY_RESET_PASSWORD, END_POINT_RESET_PASSWORD, token);
        }else{
            throw new ApiNotFound("Email no found");
        }
    }

    /**
     * Verify token to reset password.
     *
     * @throws AuthenticationApiException if token doesn't exist or can't be found
     * @param token the token
     * @return the string
     */
    public String resetPasswordVerifyToken(String token) {
        Optional<PasswordReset> passwordResetToken = passwordResetJpaRepository.findByToken(token);
        if(passwordResetToken.isPresent()){
            return "Token verified";
        }else{
            throw new AuthenticationApiException("Invalid Token");
        }
    }

    /**
     * Reset password.
     *
     * @throws ApiConflict if passwords don't match
     * @throws ApiNotFound if user doesn't exist or can't be found
     * @param passwordResetRequest the password reset request
     * @return the string
     */
    public String updatePassword(PasswordResetRequest passwordResetRequest) {
        Optional<User> userOptional = userJpaRepository.findByEmail(passwordResetRequest.getEmail());
        if(userOptional.isPresent()){
            if(passwordResetRequest.getPassword().equals(passwordResetRequest.getPasswordVerify())){
                passwordResetRequest.setPassword(passwordEncoder.encode(passwordResetRequest.getPassword()));
                User user = userMapper.toEntity(userOptional.get().getId(), userOptional.get().getUsername(), userOptional.get().getIsEnable(), userOptional.get().getRoles(), passwordResetRequest);
                userJpaRepository.save(user);
                passwordResetJpaRepository.deleteByEmail(passwordResetRequest.getEmail());
                return "Password reset successfully";
            }else{
                throw new ApiConflict("Password doesn't match");
            }
        }else{
            throw new ApiNotFound("User no found with email: "+passwordResetRequest.getEmail());
        }
    }
}
