package com.springdemo.library.security;

import com.springdemo.library.services.JwtService;
import com.springdemo.library.services.UserService;
import com.springdemo.library.utils.Common;
import com.springdemo.library.utils.Constants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserService customUserDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // Lấy jwt từ request
            String jwt = getJwt(request);

            if (StringUtils.hasText(jwt) && jwtService.validateToken(jwt)) {
                // Lấy id user từ chuỗi jwt
                String userName = jwtService.getSubjectFromJWT(jwt);
                // Lấy thông tin người dùng từ id
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(userName);
                if(userDetails != null) {
                    // Nếu người dùng hợp lệ, set thông tin cho Seturity Context
                    UsernamePasswordAuthenticationToken
                            authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                            userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception ex) {
            log.error("failed on set user authentication", ex);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwt(HttpServletRequest request) {
//        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
//        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7);
//        }
        Cookie cookie = Common.getCookie(request, Constants.JWT_NAME);
        if(cookie!=null) {
            String token = cookie.getValue();
            if(StringUtils.hasText(token)) {
                return token;
            }
        }
        return null;
    }
}
