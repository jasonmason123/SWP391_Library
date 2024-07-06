package com.springdemo.library.services;

import com.springdemo.library.security.userdetails.CustomUserDetails;
import com.springdemo.library.security.userdetails.NhanVienUserDetails;
import com.springdemo.library.services.interfaces.IAuthTokenService;
import com.springdemo.library.utils.Constants;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class JwtService implements IAuthTokenService<CustomUserDetails> {

    public String generateToken(String subject, long expiration) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        return Jwts.builder().setSubject(subject).setIssuedAt(now)
                .setExpiration(expiryDate).signWith(SignatureAlgorithm.HS256, Constants.JWT_SECRET).compact();
    }

    public String generateToken(Map<String, Object> claims, long expiration) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        return Jwts.builder().setClaims(claims).setIssuedAt(now)
                .setExpiration(expiryDate).signWith(SignatureAlgorithm.HS256, Constants.JWT_SECRET).compact();
    }

    @Override
    public String generateToken(CustomUserDetails userDetails) {
        Map<String, Object> allClaims = new HashMap<>();
        allClaims.put(Constants.TOKEN_USER_ID_CLAIM, String.valueOf(userDetails.getUser().getId()));
        allClaims.put(Constants.TOKEN_USERNAME_CLAIM, userDetails.getUser().getTenUser());
        return generateToken(allClaims, Constants.JWT_EXPIRATION);
    }

    public String generateToken(NhanVienUserDetails nhanVienDetails) {
        Map<String, Object> allClaims = new HashMap<>();
        allClaims.put(Constants.TOKEN_USER_ID_CLAIM, String.valueOf(nhanVienDetails.getNhanVien().getId()));
        allClaims.put(Constants.TOKEN_USERNAME_CLAIM, nhanVienDetails.getNhanVien().getEmail());
        return generateToken(allClaims, Constants.JWT_EXPIRATION);
    }

    public Claims getClaimsFromJWT(String token) {
        return Jwts.parser().setSigningKey(Constants.JWT_SECRET).parseClaimsJws(token).getBody();
    }

    public String getSubjectFromJWT(String token) {
        Claims claims = getClaimsFromJWT(token);
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
