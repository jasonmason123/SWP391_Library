package com.springdemo.library.controller;

import com.springdemo.library.repositories.UserRepository;
import com.springdemo.library.security.CustomUserDetails;
import com.springdemo.library.security.UserService;
import com.springdemo.library.security.jwt.JwtServices;
import com.springdemo.library.utils.Common;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.logging.Logger;

@Controller
@AllArgsConstructor
@RequestMapping
public class AuthenticationController {
    private UserRepository userRepository;
    private JwtServices jwtServices;
    private final String JWT_NAME = "saved_user_session";

    @GetMapping("/login")
    public ModelAndView authenticate() {
        return new ModelAndView("");
    }

    @GetMapping("/signup")
    public ModelAndView signup() {
        return new ModelAndView("");
    }

    @GetMapping("/logout")
    public ModelAndView logout() {
        return new ModelAndView("");
    }

    @PostMapping("/authenticate")
    @ResponseBody
    public String authenticate(
            @RequestParam("username") String userName,
            @RequestParam("password") String password
    ) {
        if(userName!=null && password!=null) {
            UserDetails customUserDetails = new UserService(userRepository).loadUserByUsername(userName);
            if(Common.sha256Hash(password).equals(customUserDetails.getPassword())) {
                return jwtServices.generateToken((CustomUserDetails) customUserDetails);
            }
        }
        return "failed";
    }

    @PostMapping("/logout")
    @ResponseBody
    public String logout(HttpServletRequest request) {
        Cookie tokenCookie = Common.getCookie(request, JWT_NAME);
        if(tokenCookie != null) {
            tokenCookie.setMaxAge(0);
        }
        return "logged-out"; //to home page
    }

    @PostMapping("/isValidUsername")
    @ResponseBody
    public String isDuplicatedUserName(@RequestParam(name = "userName") String userName) {
        if(userRepository.findUserByTenUser(userName).isPresent()) {
            return "duplicated";
        }
        return "valid";
    }
}
