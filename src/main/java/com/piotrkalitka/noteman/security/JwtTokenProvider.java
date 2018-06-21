package com.piotrkalitka.noteman.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    public String generateToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date dateNow = new Date();
        Date expiryDate = new Date(dateNow.getTime() + jwtExpirationInMs);

        try {
            return JWT.create()
                    .withSubject(Long.toString(userPrincipal.getId()))
                    .withIssuedAt(new Date())
                    .withExpiresAt(expiryDate)
                    .sign(Algorithm.HMAC256(jwtSecret));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public Long getUserIdFromJWT(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        return Long.parseLong(decodedJWT.getSubject());
    }

    public boolean validateToken(String authToken) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(authToken);
            return true;
        } catch (UnsupportedEncodingException | JWTVerificationException e) {
            return false;
        }
    }

}