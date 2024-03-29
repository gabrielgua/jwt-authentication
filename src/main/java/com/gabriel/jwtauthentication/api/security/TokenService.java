package com.gabriel.jwtauthentication.api.security;

import com.gabriel.jwtauthentication.domain.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final AuthProperties properties;


    public String generateToken(User user) {
        return buildToken(new HashMap<>(), user.getEmail(), properties.getToken().getExpiration());
    }

    public String generateToken(Map<String, Object> extraClaims, User user) {
        return buildToken(extraClaims, user.getEmail(), properties.getToken().getExpiration());
    }

    private String buildToken(Map<String, Object> extraClaims, String subject, long expiration) {
        var nowMillis = System.currentTimeMillis();

        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(subject)
                .setIssuer("jwt-authentication")
                .setIssuedAt(new Date(nowMillis))
                .setExpiration(new Date(nowMillis + expiration))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    public boolean isTokenValid(String token, String subject) {
        final String email = extractEmail(token);
        return email.equals(subject) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsTFunction) {
        final Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

        } catch (ExpiredJwtException ex) {
            throw new RuntimeException("Token is invalid");
        }
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(properties.getToken().getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
