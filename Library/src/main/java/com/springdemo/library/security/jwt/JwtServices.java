package com.springdemo.library.security.jwt;

import com.springdemo.library.security.CustomUserDetails;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class JwtServices {
    private final String JWT_SECRET = "0b2628d79427e6bce5d7313e7219bc9b8b98c2e5cb74e79135cf68fe7a18e9b1";
    private final long JWT_EXPIRATION = 604800000L; //1 week

    public String generateToken(CustomUserDetails userDetails) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);
        return Jwts.builder().setSubject(Integer.toString(userDetails.getUser().getId())).setIssuedAt(now)
                .setExpiration(expiryDate).signWith(SignatureAlgorithm.HS256, JWT_SECRET).compact();
    }

    //get userId from token
    public int getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody();
        return Integer.parseInt(claims.getSubject());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            log.error("JWT token is empty");
        }
        return false;
    }
}
