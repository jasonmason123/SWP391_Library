package com.springdemo.library.controller;

import com.springdemo.library.model.User;
import com.springdemo.library.model.dto.SigninDataDto;
import com.springdemo.library.model.dto.SignupDataDto;
import com.springdemo.library.repositories.UserRepository;
import com.springdemo.library.security.userdetails.CustomUserDetails;
import com.springdemo.library.services.EmailService;
import com.springdemo.library.services.UserService;
import com.springdemo.library.services.JwtService;
import com.springdemo.library.utils.Common;
import com.springdemo.library.utils.Constants;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.regex.Matcher;

@Controller
@Slf4j
@RequestMapping
public class CustomerAuthenticationController extends AbstractAuthenticationController {
    private UserRepository userRepository;

    public CustomerAuthenticationController(JwtService jwtService, EmailService emailService, UserRepository userRepository) {
        super(jwtService, emailService);
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public ModelAndView login(Authentication authentication) {
        if(Common.isAuthenticated(authentication)) {
            return new ModelAndView("redirect:/home");
        }
        return new ModelAndView("Layout")
        .addObject("title", "Đăng nhập")
        .addObject("includedPage", "login-page")
        .addObject("breadcrumb", """
                                    <ul>
                                        <li><a href="#">Trang chủ</a></li>
                                        <li><a href="#" class="active">Đăng nhập</a></li>
                                    </ul>""");
    }

    @GetMapping("/signup")
    public ModelAndView signup(Authentication authentication) {
        if(Common.isAuthenticated(authentication)) {
            return new ModelAndView("redirect:/home");
        }
        ModelAndView signupViewModel = new ModelAndView("Layout");
        signupViewModel.addObject("breadcrumb", """
                                    <ul>
                                        <li><a href="#">Trang chủ</a></li>
                                        <li><a href="#" class="active">Đăng ký</a></li>
                                    </ul>""");
        signupViewModel.addObject("title", "Đăng ký");
        signupViewModel.addObject("includedPage", "signup-page");
        return signupViewModel;
    }

    @Override
    @GetMapping("/logout")
    public String logout(SecurityContextLogoutHandler securityContextLogoutHandler, Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        customLogout(securityContextLogoutHandler, authentication, request, response);
        return "redirect:/login";
    }

    @Override
    @GetMapping("/forgotpassword")
    public ModelAndView forgotPassword() {
        return new ModelAndView("forgot-password-page");
    }

    @Override
    @GetMapping("/changepassword")
    public ModelAndView changePassword(
            @RequestParam(name = "auth") String auth
    ) {
        String email = getEmailFromAuthToken(auth);
        return (email!=null  && isExistEmail(email)) ?
                new ModelAndView("change-password-page") : new ModelAndView("redirect:/error");
    }

    @Override
    @PostMapping("/processlogin")
    @ResponseBody
    public ResponseEntity<String> processLogin(
            @RequestBody SigninDataDto signinDataDto,
            HttpServletResponse response
    ) {
        try {
            if(signinDataDto.getUserName()!=null && signinDataDto.getPassword()!=null) {
                String userName = signinDataDto.getUserName();
                String password = Common.sha256Hash(signinDataDto.getPassword());
                boolean rememberMe = signinDataDto.isRememberMe();
                UserDetails customUserDetails = UserService.builder()
                        .userRepository(userRepository).build().loadUserByUsername(userName);
                if(password.equals(customUserDetails.getPassword()) && customUserDetails.isEnabled()) {
                    Cookie jwtCookie = new Cookie(Constants.JWT_COOKIE_NAME, jwtService.generateToken((CustomUserDetails) customUserDetails));
                    if(rememberMe) {
                        jwtCookie.setMaxAge(7*24*60*60);
                    }
                    jwtCookie.setPath("/");
                    jwtCookie.setHttpOnly(true);
                    response.addCookie(jwtCookie);
                    return ResponseEntity.ok().build();
                }
            }
        } catch (NullPointerException e) {
            log.error("Error: " + e);
        }
        log.error("Login failed!");
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/processsignup")
    @ResponseBody
    public ResponseEntity<String> processSignup(
            @RequestBody SignupDataDto signupDataDto,
            HttpServletResponse response
    ) {
        try {
            String tenUser = signupDataDto.getTenUser().trim();
            String email = signupDataDto.getEmail().trim();
            String soDienThoai = signupDataDto.getSoDienThoai().trim();
            String soCCCD = signupDataDto.getSoCCCD().trim();
            if(Constants.VALID_USERNAME_REGEX.matcher(tenUser).matches() &&
                    Constants.VALID_EMAIL_ADDRESS_REGEX.matcher(email).matches() &&
                    Constants.VALID_SODIENTHOAI_REGEX.matcher(soDienThoai).matches() &&
                    Constants.VALID_SOCCCD_REGEX.matcher(soCCCD).matches()
            ) {
                User newUser = new User(tenUser, email, null, soDienThoai, soCCCD, new Date());
                newUser.setMatKhau(Common.sha256Hash(signupDataDto.getMatKhau()));
                userRepository.save(newUser);
                return this.processLogin(new SigninDataDto(tenUser, signupDataDto.getMatKhau(), false), response);
            }
        } catch (DataIntegrityViolationException | NullPointerException e) {
            log.error("Error: " + e);
        }
        return ResponseEntity.badRequest().build();
    }

    @Override
    @PostMapping("/processforgotpassword")
    @ResponseBody
    public ModelAndView processForgotPassword(
        @RequestParam(name = "auth") String auth,
        @RequestParam(name = "new") String newPassword)
     {
        String email = getEmailFromAuthToken(auth);
        if(email != null) {
            User foundUser = userRepository.findUserByEmail(email).orElse(null);
            if(foundUser!=null) {
                foundUser.setMatKhau(Common.sha256Hash(newPassword));
                userRepository.save(foundUser);
                return new ModelAndView("redirect:/login");
            }
        }
        log.error("email not found or invalid");
        return new ModelAndView("redirect:/error");
    }

//Utils_________________________________________________________________________________________________________________

    @Override
    @PostMapping("/auth")
    @ResponseBody
    public ResponseEntity<String> sendChangePasswordEmail(
            @RequestParam(name = "email") String email)
    {
        return super.sendChangePasswordEmail(email);
    }

    @Override
    protected boolean isExistEmail(String email) {
        return userRepository.findUserByEmail(email).isPresent();
    }

    @PostMapping("/isvalidemail")
    @ResponseBody
    public ResponseEntity<String> isValidEmail(@RequestParam(name = "email") String email) {
        email = email.trim();
        Matcher matcher = Constants.VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        if(matcher.matches()) {
            return isExistEmail(email) ? ResponseEntity.ok(Constants.DATA_EXISTED) : ResponseEntity.ok(Constants.DATA_NOT_EXIST);
        } else {
            return ResponseEntity.ok(Constants.DATA_PATTERN_UNMATCHED);
        }
    }

    @PostMapping("/isvalidsodienthoai")
    @ResponseBody
    public ResponseEntity<String> isValidSoDienThoai(@RequestParam(name = "sodienthoai") String soDienThoai) {
        soDienThoai = soDienThoai.trim();
        Matcher matcher = Constants.VALID_SODIENTHOAI_REGEX.matcher(soDienThoai);
        if(matcher.matches()) {
            return userRepository.findUserBySoDienThoai(soDienThoai).isPresent() ?
                    ResponseEntity.ok(Constants.DATA_EXISTED) : ResponseEntity.ok(Constants.DATA_NOT_EXIST);
        } else {
            return ResponseEntity.ok(Constants.DATA_PATTERN_UNMATCHED);
        }
    }

    @PostMapping("/isvalidsocccd")
    @ResponseBody
    public ResponseEntity<String> isValidSoCCCD(@RequestParam(name = "socccd") String soCCCD) {
        soCCCD = soCCCD.trim();
        Matcher matcher = Constants.VALID_SOCCCD_REGEX.matcher(soCCCD);
        if(matcher.matches()) {
            return userRepository.findUserBySoCCCD(soCCCD).isPresent() ?
                    ResponseEntity.ok(Constants.DATA_EXISTED) : ResponseEntity.ok(Constants.DATA_NOT_EXIST);
        } else {
            return ResponseEntity.ok(Constants.DATA_PATTERN_UNMATCHED);
        }
    }

    @PostMapping("/isvalidtenuser")
    @ResponseBody
    public ResponseEntity<String> isValidTenUser(@RequestParam(name = "tenuser") String userName) {
        userName = userName.trim();
        Matcher matcher = Constants.VALID_USERNAME_REGEX.matcher(userName);
        if (matcher.matches()) {
            return userRepository.findUserByTenUser(userName).isPresent() ?
                    ResponseEntity.ok(Constants.DATA_EXISTED) : ResponseEntity.ok(Constants.DATA_NOT_EXIST);
        } else {
            return ResponseEntity.ok(Constants.DATA_PATTERN_UNMATCHED);
        }
    }

    @Override
    @PostMapping("/sendotp")
    @ResponseBody
    public ResponseEntity<String> sendOtp(
        @RequestParam(name = "receiver") String receiver,
        HttpServletRequest request
    ) {
        return super.sendOtp(receiver, request);
    }
}
