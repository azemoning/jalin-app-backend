package com.jalin.jalinappbackend.module.authentication.jwt;

import com.jalin.jalinappbackend.module.authentication.entity.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenUtility {
    private static final long ONE_DAY_IN_MILLISECONDS = 24 * 60 * 60 * 1000;
    @Value("${jwt.signing.key}")
    private String jwtSigningKey;

    public String generateToken(UserDetailsImpl user) {
        return Jwts.builder()
                .setSubject(user.getUser().getUserId().toString())
                .claim("email", user.getUsername())
                .claim("authorities", user.getAuthorities())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ONE_DAY_IN_MILLISECONDS))
                .signWith(Keys.hmacShaKeyFor(jwtSigningKey.getBytes()))
                .compact();
    }

    public Boolean validate(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtSigningKey.getBytes()))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException exception) {
            log.error("Invalid JWT signature - {}", exception.getMessage());
        } catch (MalformedJwtException exception) {
            log.error("Invalid JWT token - {}", exception.getMessage());
        } catch (ExpiredJwtException exception) {
            log.error("Expired JWT token - {}", exception.getMessage());
        } catch (UnsupportedJwtException exception) {
            log.error("Unsupported JWT token - {}", exception.getMessage());
        } catch (IllegalArgumentException exception) {
            log.error("JWT claims string is empty - {}", exception.getMessage());
        }
        return false;
    }

    public String getEmail(String token) {
        Claims claims = getClaims(token);
        return (String) claims.get("email");
    }

    public Set<SimpleGrantedAuthority> getAuthorities(String token) {
        Claims claims = getClaims(token);
        List<Map<String, String>> authorities = (List<Map<String, String>>) claims.get("authorities");
        Set<SimpleGrantedAuthority> simpleGrantedAuthoritySet = authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.get("authority")))
                .collect(Collectors.toSet());
        return simpleGrantedAuthoritySet;
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSigningKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
