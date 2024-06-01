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
            String jwt = getJwt(request);

            if (StringUtils.hasText(jwt) && jwtService.validateToken(jwt)) {
                String userName = jwtService.getSubjectFromJWT(jwt);
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(userName);
                if(userDetails != null) {
                    //Spring Boot có một filter mặc định là UsernameAndPasswordAuthenticationFilter. Filter Jwt này sẽ được đặt
                    //trước UsernameAndPasswordAuthenticationFilter để lấy ra thông tin người dùng (gồm tên đăng nhập và mật khẩu)
                    //từ Jwt token và gửi thông tin đó cho UsernameAndAuthenticationFilter để xác thực
                    //(bằng cách set thông tin cho Security Context)
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

        //Đi tiếp đến UsernameAndPasswordAuthenticationFilter. Khi đăng nhập thất bại, không phải do
        // JwtAuthenticationFilter từ chối, mà do UsernameAndPasswordAuthenticationFilter từ chối
        filterChain.doFilter(request, response);
    }

    private String getJwt(HttpServletRequest request) {
//        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
//        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7);
//        }
        Cookie cookie = Common.getCookie(request, Constants.JWT_COOKIE_NAME);
        if(cookie!=null) {
            String token = cookie.getValue();
            if(StringUtils.hasText(token)) {
                return token;
            }
        }
        return null;
    }
}
