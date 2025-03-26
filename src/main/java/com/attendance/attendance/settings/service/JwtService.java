package com.attendance.attendance.settings.service;

import com.attendance.attendance.settings.dto.JwtUser;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(JwtUser jwtUser) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userID", jwtUser.getUserID());
        claims.put("email", jwtUser.getEmail());
        claims.put("name", jwtUser.getName());
        LocalDateTime now = LocalDateTime.now();
        return createToken(claims);
    }

    private String createToken(Map<String, Object> claims) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration);

        byte[] signingKey = Base64.getEncoder().encode(secret.getBytes(StandardCharsets.UTF_8));
        SecretKeySpec key = new SecretKeySpec(signingKey, SignatureAlgorithm.HS256.getJcaName());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(key)
                .compact();
    }
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        byte[] signingKey = Base64.getEncoder().encode(secret.getBytes(StandardCharsets.UTF_8));
        SecretKeySpec key = new SecretKeySpec(signingKey, SignatureAlgorithm.HS256.getJcaName());

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenExpired(String token) {
        final Date expiration = extractExpiration(token);
        return expiration.before(new Date());
    }

    public boolean validateToken(String token) {
        try {
            if (isTokenExpired(token)) {
                return false;
            }
            byte[] signingKey = Base64.getEncoder().encode(secret.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec key = new SecretKeySpec(signingKey, SignatureAlgorithm.HS256.getJcaName());

            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException | MalformedJwtException  e) {
            return false;
        }
    }
    public Long extractUserID(String token) {
        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7); // Remove "Bearer " prefix
            }

            Claims claims = extractAllClaims(token);
            return claims.get("userID", Long.class);

        } catch (ExpiredJwtException | MalformedJwtException e) {
            return null;
        }
    }
}
