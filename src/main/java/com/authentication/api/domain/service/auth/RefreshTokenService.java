package com.authentication.api.domain.service.auth;

import com.authentication.api.domain.exception.AuthenticationApiException;
import com.authentication.api.infrastructure.persistense.entity.RefreshToken;
import com.authentication.api.infrastructure.persistense.jpa.RefreshTokenJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * The Refresh token service.
 */
@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {

    private final RefreshTokenJpaRepository refreshTokenJpaRepository;

    /**
     * Generate refresh token.
     *
     * @return the refresh token
     */
    public RefreshToken generateRefreshToken(){
        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .createdAt(Instant.now())
                .build();
        return refreshTokenJpaRepository.save(refreshToken);
    }

    /**
     * Validate refresh token.
     *
     * @throws AuthenticationApiException if token is invalid or can't be found
     * @param token the token
     */
    void validateRefreshToken(String token){
        Optional<RefreshToken> refreshTokenOptinal = refreshTokenJpaRepository.findByToken(token);
        if(!refreshTokenOptinal.isPresent()){
            throw new AuthenticationApiException("Invalid refresh token");
        }
    }

    /**
     * Delete refresh token.
     *
     * @throws AuthenticationApiException if refresh token is invalid or can't be found
     * @param token the token
     */
    public void deleteRefreshToken(String token){
        Optional<RefreshToken> refreshTokenOptinal = refreshTokenJpaRepository.findByToken(token);
        if(!refreshTokenOptinal.isPresent()){
            throw new AuthenticationApiException("Invalid refresh token");
        }else{
            refreshTokenJpaRepository.deleteByToken(token);
        }
    }
}
