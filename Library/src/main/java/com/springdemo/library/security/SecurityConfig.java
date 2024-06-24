package com.springdemo.library.security;

import com.springdemo.library.security.filters.JwtAuthenticationFilter;
import com.springdemo.library.security.filters.OtpAuthenticationFilter;
import com.springdemo.library.utils.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }
    @Bean
    public OtpAuthenticationFilter otpAuthenticationFilter() {
        return new OtpAuthenticationFilter();
    }

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/login", "/processlogin", "/sendotp",
                                        "/signup", "/processsignup", "/auth", "/changepassword",
                                        "/forgotpassword", "/processforgotpassword",
                                        "/isvalidemail", "/isvalidsodienthoai",
                                        "/isvalidsocccd", "/isvalidtenuser",
                                        "/home", "/book/**", "/blog/**").permitAll()
                                .requestMatchers("/management/**").permitAll()
                                //.requestMatchers("/cart/**").permitAll()
                                //0:Admin, 1:Staff, 2:Customer
                                //.requestMatchers("/").hasRole("ROLE_0")
                                //.requestMatchers("/").hasRole("ROLE_1")
                                //.requestMatchers("/").hasRole("ROLE_CUSTOMER")
                                .anyRequest().authenticated()
                ).logout(logout -> logout
                        .logoutUrl("/Library/logout").permitAll()
                        .deleteCookies("JSESSIONID", Constants.JWT_COOKIE_NAME)
                        .clearAuthentication(true)
                );
                //.formLogin(formLogin -> formLogin.loginPage("/login").permitAll());
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(otpAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/css/**", "/js/**", "/img/**", "/fonts/**", "/static-admin_and_staff/**");
    }
}
