package com.springdemo.library.services;

import com.springdemo.library.security.CustomUserDetails;
import com.springdemo.library.services.interfaces.IAuthTokenService;
import com.springdemo.library.utils.Constants;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class JwtService implements IAuthTokenService<CustomUserDetails> {

    public String generateToken(String subject, long expiration) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        return Jwts.builder().setSubject(subject).setIssuedAt(now)
                .setExpiration(expiryDate).signWith(SignatureAlgorithm.HS256, Constants.JWT_SECRET).compact();
    }

    @Override
    public String generateToken(CustomUserDetails userDetails) {
        return generateToken(userDetails.getUser().getTenUser(), Constants.JWT_EXPIRATION);
    }

    //get userId from token
    public String getSubjectFromJWT(String token) {
        Claims claims = Jwts.parser().setSigningKey(Constants.JWT_SECRET).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(Constants.JWT_SECRET).parseClaimsJws(token);
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
