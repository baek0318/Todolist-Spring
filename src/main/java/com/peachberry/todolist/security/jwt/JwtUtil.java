package com.peachberry.todolist.security.jwt;

import com.peachberry.todolist.security.domain.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtUtil {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private final int ACCESS_TOKEN_TIME = 3600000*2;

    private final int REFRESH_TOKEN_TIME = 3600000*24*14;

    public String accessTokenGenerate(Authentication authentication) {
        return generateJwtToken(authentication, ACCESS_TOKEN_TIME);
    }

    public String accessTokenGenerate(String email) {
        return generateJwtToken(email, ACCESS_TOKEN_TIME);
    }

    public String refreshTokenGenerate(Authentication authentication) {
        return generateJwtToken(authentication, REFRESH_TOKEN_TIME);
    }

    private String generateJwtToken(Authentication authentication, int time) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getEmail()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + time))
                .signWith(key)
                .compact();
    }

    private String generateJwtToken(String email, int time) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + time))
                .signWith(key)
                .compact();
    }

    public boolean checkAccessTokenExpire(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
            return false;
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired {}",e.getMessage());
        }
        return true;
    }

    public String getEmailFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}
