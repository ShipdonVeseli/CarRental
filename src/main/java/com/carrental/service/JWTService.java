package com.carrental.service;

import com.carrental.entity.exception.JWTParseException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service
public class JWTService {

    private final Key signingKey;

    public JWTService() {
        signingKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    public String buildJWT(Long userID) {
        return Jwts.builder()
                .setSubject(userID.toString())
                .signWith(signingKey)
                .compact();
    }

    public Long parseJWT(String jwt) throws JWTParseException {
        Jws<Claims> parsedJwt = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(jwt);
        return Long.valueOf(parsedJwt.getBody().getSubject());
    }
}
