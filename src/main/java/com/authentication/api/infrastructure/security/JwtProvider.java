package com.authentication.api.infrastructure.security;

import com.authentication.api.domain.exception.AuthenticationApiException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.time.Instant;
import java.util.Date;

import static io.jsonwebtoken.Jwts.parser;

/**
 * The type Jwt provider.
 */
@Service
public class JwtProvider {

    @Value("${jks.secret.password}")
    private String secret;

    @Value("${jwt.expiration.time}")
    private Long jwtExpirationTimeMillis;

    private KeyStore keyStore;

    /**
     * Init.
     * Read the jks file
     *
     * @throws AuthenticationApiException if can't read the jks
     */
    @PostConstruct
    public void init() {
        try {
            keyStore = KeyStore.getInstance("JKS");
            InputStream resourceAsStream = getClass().getResourceAsStream("/apiauth.jks");
            keyStore.load(resourceAsStream, secret.toCharArray());
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            throw new AuthenticationApiException("Exception occurred while loading keystore", e);
        }
    }

    /**
     * Generate token string.
     *
     * @param authentication the authentication
     * @return the jwt
     */
    public String generateToken(Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(principal.getUsername())
                .setIssuedAt(Date.from(Instant.now()))
                .signWith(getPrivateKey())
                .setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationTimeMillis)))
                .compact();
    }

    /**
     * Generate token with username string.
     *
     * @param username the username
     * @return the jwt
     */
    public String generateTokenWithUsername(String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(Instant.now()))
                .signWith(getPrivateKey())
                .setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationTimeMillis)))
                .compact();
    }

    /**
     * Get the private key from JKS
     *
     * @throws AuthenticationApiException if can't get the public key from jks
     * @return private key
     */
    private Key getPrivateKey() {
        try {
            return keyStore.getKey("apiauth", secret.toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new AuthenticationApiException("Exception occurred while retrieving public key from keystore", e);
        }
    }

    /**
     * Gets jwt expiration millis.
     *
     * @return the jwt expiration millis
     */
    public Long getJwtExpirationMillis() {
        return jwtExpirationTimeMillis;
    }

    /**
     * Validate token boolean.
     *
     * @param jwt the jwt
     * @return the boolean
     */
    public boolean validateToken(String jwt) {
        parser().setSigningKey(getPublicKey()).parseClaimsJws(jwt);
        return true;
    }

    /**
     * Get the public key from the JKS
     *
     * @throws AuthenticationApiException if can't read the public key
     * @return the public key from the keystore
     */
    private PublicKey getPublicKey(){
        try{
            return keyStore.getCertificate("apiauth").getPublicKey();
        }catch (KeyStoreException keyStoreException){
            throw new AuthenticationApiException("Exception occurred while retrieving public key");
        }
    }

    /**
     * Gets username from jwt.
     *
     * @param jwt the jwt
     * @return the username from jwt
     */
    public String getUsernameFromJwt(String jwt) {
        Claims claims = parser()
                .setSigningKey(getPublicKey())
                .parseClaimsJws(jwt)
                .getBody();
        return claims.getSubject();
    }
}


