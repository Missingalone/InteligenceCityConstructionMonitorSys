package com.cyx.auth.common_security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class JwtTokenService {

    private final SecretKey signingKey;
    private final long expiration;

    public JwtTokenService(@Value("${auth.jwt.secret}") String secret,
                           @Value("${auth.jwt.expiration}") long expiration) {
        this.signingKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
        this.expiration = expiration;
    }

    public String createToken(UserDetails userDetails) {
        Instant now = Instant.now();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> authority.startsWith("ROLE_"))
                .map(authority -> authority.substring(5))
                .toList();
        List<String> permissions = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> !authority.startsWith("ROLE_"))
                .toList();

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("roles", roles)
                .claim("permissions", permissions)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(expiration)))
                .signWith(signingKey)
                .compact();
    }

    public Optional<Claims> parseToken(String token) {
        try {
            return Optional.of(Jwts.parser().verifyWith(signingKey).build()
                    .parseSignedClaims(token).getPayload());
        } catch (JwtException | IllegalArgumentException exception) {
            return Optional.empty();
        }
    }

    public List<String> getRoles(Claims claims) {
        return getStringList(claims, "roles");
    }

    public List<String> getPermissions(Claims claims) {
        return getStringList(claims, "permissions");
    }

    private List<String> getStringList(Claims claims, String key) {
        List<?> values = claims.get(key, List.class);
        return values == null ? List.of() : values.stream().map(Object::toString).toList();
    }
}
