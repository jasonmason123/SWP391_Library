package com.springdemo.library.controller;

import com.springdemo.library.model.User;
import com.springdemo.library.model.dto.OtpDto;
import com.springdemo.library.model.dto.SigninDataDto;
import com.springdemo.library.model.dto.SignupDataDto;
import com.springdemo.library.repositories.UserRepository;
import com.springdemo.library.security.CustomUserDetails;
import com.springdemo.library.services.EmailService;
import com.springdemo.library.services.UserService;
import com.springdemo.library.services.JwtService;
import com.springdemo.library.utils.Common;
import com.springdemo.library.utils.Constants;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@Slf4j
@AllArgsConstructor
@RequestMapping
public class AuthenticationController {
    private UserRepository userRepository;
    private JwtService jwtService;
    private EmailService emailService;

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private static final Pattern VALID_SOCCCD_REGEX =
            Pattern.compile("^(?=.{9,12}$)[0-9]+$", Pattern.CASE_INSENSITIVE);
    private static final Pattern VALID_SODIENTHOAI_REGEX =
            Pattern.compile("^(?=.{10}$)[0-9]+$", Pattern.CASE_INSENSITIVE);
    private static final Pattern VALID_USERNAME_REGEX =
            Pattern.compile("^(?=.{5,20}$)[a-zA-Z0-9_-]+$", Pattern.CASE_INSENSITIVE);

    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView loginViewModel = new ModelAndView("Layout");
        loginViewModel.addObject("breadcrumb", "<ul>\n" +
                "                        <li><a href=\"#\">Trang chủ</a></li>\n" +
                "                        <li><a href=\"#\" class=\"active\">Đăng nhập</a></li>\n" +
                "                    </ul>");
        loginViewModel.addObject("title", "Đăng nhập");
        loginViewModel.addObject("includedPage", "login-page");
        return loginViewModel;
    }

    @GetMapping("/signup")
    public ModelAndView signup() {
        ModelAndView signupViewModel = new ModelAndView("Layout");
        signupViewModel.addObject("breadcrumb", "<ul>\n" +
                "                        <li><a href=\"#\">Trang chủ</a></li>\n" +
                "                        <li><a href=\"#\" class=\"active\">Đăng ký</a></li>\n" +
                "                    </ul>");
        signupViewModel.addObject("title", "Đăng ký");
        signupViewModel.addObject("includedPage", "signup-page");
        return signupViewModel;
    }

    @GetMapping("/logout")
    public ModelAndView logout() {
        return new ModelAndView("");
    }

    @GetMapping("/forgotpassword")
    public ModelAndView forgotPassword() {
        ModelAndView forgotPasswordViewModel = new ModelAndView("Layout");
        forgotPasswordViewModel.addObject("breadcrumb", "<ul>\n" +
                "                        <li><a href=\"#\">Trang chủ</a></li>\n" +
                "                        <li><a href=\"#\" class=\"active\">Quên mật khẩu</a></li>\n" +
                "                    </ul>");
        forgotPasswordViewModel.addObject("title", "Quên mật khẩu");
        forgotPasswordViewModel.addObject("includedPage", "forgot-password-page");
        return forgotPasswordViewModel;
    }

    @GetMapping("/changepassword")
    public ModelAndView changePassword(
            @RequestParam(name = "auth") String auth
    ) {
        String email = getEmailFromAuthToken(auth);
        if(email!=null  && userRepository.findUserByEmail(email).isPresent()) {
            ModelAndView changePasswordViewModel = new ModelAndView("Layout");
            changePasswordViewModel.addObject("breadcrumb", "<ul>\n" +
                    "                        <li><a href=\"#\">Trang chủ</a></li>\n" +
                    "                        <li><a href=\"#\" class=\"active\">Đổi mật khẩu</a></li>\n" +
                    "                    </ul>");
            changePasswordViewModel.addObject("title", "Đổi mật khẩu");
            changePasswordViewModel.addObject("includedPage", "change-password-page");
            changePasswordViewModel.addObject("auth", auth);
            return changePasswordViewModel;
        }
        return new ModelAndView("redirect:/error");
    }

    @PostMapping("/sendotp")
    @ResponseBody
    public ResponseEntity<String> sendOtp(
            @RequestParam(name = "receiver") String receiver,
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession();
        if(session.getAttribute("otp")!=null) {
            session.removeAttribute("otp");
        }
        session.setMaxInactiveInterval(2*60);
        OtpDto otp = emailService.sendOtpViaEmail(receiver);
        session.setAttribute("otp", otp);
        if(otp!=null) {
            return ResponseEntity.ok().build(); //proceed
        }
        return ResponseEntity.badRequest().build(); //error
    }

    @PostMapping("/auth")
    @ResponseBody
    public ResponseEntity<String> generateChangePasswordToken(
            @RequestParam(name = "email") String email
    ) {
        if(isEmailExist(email)) {
            return ResponseEntity.ok(jwtService.generateToken(email, 60*60*1000));
        }
        return ResponseEntity.badRequest().build();
    }

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
                UserDetails customUserDetails = new UserService(userRepository).loadUserByUsername(userName);
                if(password.equals(customUserDetails.getPassword())) {
                    Cookie jwtCookie = new Cookie(Constants.JWT_NAME, jwtService.generateToken((CustomUserDetails) customUserDetails));
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

    @PostMapping("/processlogout")
    @ResponseBody
    public ResponseEntity<String> processLogout(HttpServletRequest request) {
        Cookie tokenCookie = Common.getCookie(request, Constants.JWT_NAME);
        if(tokenCookie != null) {
            tokenCookie.setMaxAge(0);
        }
        request.getSession().invalidate();
        return ResponseEntity.ok("logout_success"); //to home page
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
            if(VALID_USERNAME_REGEX.matcher(tenUser).matches() &&
                    VALID_EMAIL_ADDRESS_REGEX.matcher(email).matches() &&
                    VALID_SODIENTHOAI_REGEX.matcher(soDienThoai).matches() &&
                    VALID_SOCCCD_REGEX.matcher(soCCCD).matches()
            ) {
                User newUser = new User(tenUser, email, "2", null, soDienThoai, soCCCD);
                newUser.setMatKhau(Common.sha256Hash(signupDataDto.getMatKhau()));
                userRepository.save(newUser);
                return processLogin(new SigninDataDto(tenUser, signupDataDto.getMatKhau(), false), response);
            }
        } catch (DataIntegrityViolationException | NullPointerException e) {
            log.error("Error: " + e);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/processforgotpassword")
    @ResponseBody
    public ResponseEntity<String> processForgotPassword(
            @RequestParam(name = "auth") String auth,
            @RequestParam(name = "new") String newPassword
    ) {
        String email = getEmailFromAuthToken(auth);
        if(email != null) {
            User foundUser = userRepository.findUserByEmail(email).orElse(null);
            if(foundUser!=null) {
                foundUser.setMatKhau(Common.sha256Hash(newPassword));
                userRepository.save(foundUser);
                return ResponseEntity.ok().build();
            }
        }
        log.error("email not found or invalid");
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/isvalidemail")
    @ResponseBody
    public ResponseEntity<String> isExistedEmail(@RequestParam(name = "email") String email) {
        email = email.trim();
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        if(matcher.matches()) {
            return isEmailExist(email) ? ResponseEntity.ok("existed") : ResponseEntity.ok("notExist");
        } else {
            return ResponseEntity.ok("unmatched");
        }
    }

    @PostMapping("/isvalidsodienthoai")
    @ResponseBody
    public ResponseEntity<String> isExistedSoDienThoai(@RequestParam(name = "sodienthoai") String soDienThoai) {
        soDienThoai = soDienThoai.trim();
        Matcher matcher = VALID_SODIENTHOAI_REGEX.matcher(soDienThoai);
        if(matcher.matches()) {
            return userRepository.findUserBySoDienThoai(soDienThoai).isPresent() ?
                    ResponseEntity.ok("existed") : ResponseEntity.ok("notExist");
        } else {
            return ResponseEntity.ok("unmatched");
        }
    }

    @PostMapping("/isvalidsocccd")
    @ResponseBody
    public ResponseEntity<String> isExistedSoCCCD(@RequestParam(name = "socccd") String soCCCD) {
        soCCCD = soCCCD.trim();
        Matcher matcher = VALID_SOCCCD_REGEX.matcher(soCCCD);
        if(matcher.matches()) {
            return userRepository.findUserBySoCCCD(soCCCD).isPresent() ?
                    ResponseEntity.ok("existed") : ResponseEntity.ok("notExist");
        } else {
            return ResponseEntity.ok("unmatched");
        }
    }

    @PostMapping("/isvalidtenuser")
    @ResponseBody
    public ResponseEntity<String> isExistedTenUser(@RequestParam(name = "tenuser") String userName) {
        userName = userName.trim();
        Matcher matcher = VALID_USERNAME_REGEX.matcher(userName);
        if(matcher.matches()) {
            return userRepository.findUserByTenUser(userName).isPresent() ?
                    ResponseEntity.ok("existed") : ResponseEntity.ok("notExist");
        } else {
            return ResponseEntity.ok("unmatched");
        }
    }

    private boolean isEmailExist(String email) {
        return userRepository.findUserByEmail(email).isPresent();
    }

    private String getEmailFromAuthToken(String emailToken) {
        return jwtService.validateToken(emailToken) ? jwtService.getSubjectFromJWT(emailToken) : null;
    }
}
