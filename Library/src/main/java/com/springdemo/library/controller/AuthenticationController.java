package com.springdemo.library.controller;

import com.springdemo.library.model.User;
import com.springdemo.library.model.dto.EmailDetailsDto;
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
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            return new ModelAndView("redirect:/home");
        }
        ModelAndView loginViewModel = new ModelAndView("Layout")
        .addObject("title", "Đăng nhập")
        .addObject("includedPage", "login-page")
        .addObject("breadcrumb", "<ul>\n" +
                "                        <li><a href=\"#\">Trang chủ</a></li>\n" +
                "                        <li><a href=\"#\" class=\"active\">Đăng nhập</a></li>\n" +
                "                    </ul>");
        return loginViewModel;
    }

    @GetMapping("/signup")
    public ModelAndView signup() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            return new ModelAndView("redirect:/home");
        }
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
    public String logout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        Cookie tokenCookie = Common.getCookie(request, Constants.JWT_COOKIE_NAME);
        if(tokenCookie != null) {
            tokenCookie.setMaxAge(0);
            tokenCookie.setPath("/");
            response.addCookie(tokenCookie);
        }
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, authentication);
        SecurityContextHolder.clearContext();
        return "redirect:/login";
    }

    @GetMapping("/forgotpassword")
    public ModelAndView forgotPassword() {
        return new ModelAndView("forgot-password-page");
    }

    @GetMapping("/changepassword")
    public ModelAndView changePassword(
        @RequestParam(name = "auth") String auth
    ) {
        String email = getEmailFromAuthToken(auth);
        return (email!=null  && isExistEmail(email)) ?
                new ModelAndView("change-password-page") : new ModelAndView("redirect:/error");
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
    public ResponseEntity<String> sendChangePasswordEmail(
        @RequestParam(name = "email") String email
    ) {
        if(isExistEmail(email)) {
            log.warn("Sent email to: " + email);
            String token = jwtService.generateToken(email, 60*60*1000);
            String link = Constants.CONTEXT_PATH + "/changepassword?auth=" + token;
            return emailService.sendToUser(EmailDetailsDto.builder().recipient(email).subject("Đổi mật khẩu")
                    .messageBody("Visit this url to change password: " + link).build()) //Đổi body thành định dạng html khi đã đẩy lên server
                    ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
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

//    @PostMapping("/processlogout")
//    @ResponseBody
//    public ResponseEntity<String> processLogout(HttpServletRequest request) {
//        Cookie tokenCookie = Common.getCookie(request, Constants.JWT_NAME);
//        if(tokenCookie != null) {
//            tokenCookie.setMaxAge(0);
//        }
//        request.getSession().invalidate();
//        return ResponseEntity.ok("logout_success"); //to home page
//    }

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
    public ModelAndView processForgotPassword(
        @RequestParam(name = "auth") String auth,
        @RequestParam(name = "new") String newPassword
    ) {
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

    @PostMapping("/isvalidemail")
    @ResponseBody
    public ResponseEntity<String> isExistedEmail(@RequestParam(name = "email") String email) {
        email = email.trim();
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        if(matcher.matches()) {
            return isExistEmail(email) ? ResponseEntity.ok("existed") : ResponseEntity.ok("notExist");
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

    private boolean isExistEmail(String email) {
        return userRepository.findUserByEmail(email).isPresent();
    }

    private String getEmailFromAuthToken(String emailToken) {
        return jwtService.validateToken(emailToken) ? jwtService.getSubjectFromJWT(emailToken) : null;
    }
}
