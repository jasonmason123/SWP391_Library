package com.springdemo.library.security;

import com.springdemo.library.model.dto.OtpDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;

public class OtpAuthenticationFilter extends OncePerRequestFilter {
    private final RequestMatcher processSignUpMatcher = new AntPathRequestMatcher("/processsignup");
    private final RequestMatcher changePasswordMatcher = new AntPathRequestMatcher("/changepassword");
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (this.processSignUpMatcher.matches(request) || this.changePasswordMatcher.matches(request)) {
            HttpSession session = request.getSession();
            OtpDto sourceOtp = session.getAttribute("otp")!=null ? (OtpDto) session.getAttribute("otp") : null;
            String inputOtp = request.getHeader("otpInput");
            if(sourceOtp!=null && sourceOtp.getOtp().equals(inputOtp) && new Date().before(sourceOtp.getExpiryDate())) {
                session.removeAttribute("otp");
                response.setStatus(HttpServletResponse.SC_OK);
                filterChain.doFilter(request, response);
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
