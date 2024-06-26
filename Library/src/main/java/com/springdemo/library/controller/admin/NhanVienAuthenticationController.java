package com.springdemo.library.controller.admin;

import com.springdemo.library.controller.AbstractAuthenticationController;
import com.springdemo.library.model.NhanVien;
import com.springdemo.library.model.dto.SigninDataDto;
import com.springdemo.library.repositories.NhanVienRepository;
import com.springdemo.library.security.userdetails.NhanVienUserDetails;
import com.springdemo.library.services.EmailService;
import com.springdemo.library.services.JwtService;
import com.springdemo.library.services.UserService;
import com.springdemo.library.utils.Common;
import com.springdemo.library.utils.Constants;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.regex.Matcher;

@Controller
@Slf4j
@RequestMapping("/management")
public class NhanVienAuthenticationController extends AbstractAuthenticationController {

    private NhanVienRepository nhanVienRepository;

    public NhanVienAuthenticationController(JwtService jwtService, EmailService emailService, NhanVienRepository nhanVienRepository) {
        super(jwtService, emailService);
        this.nhanVienRepository = nhanVienRepository;
    }

    @Override
    @GetMapping("/login")
    public ModelAndView login(Authentication authentication) {
        if(Common.isAuthenticated(authentication)
            && authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_0") || role.getAuthority().equals("ROLE_1"))
        ) {
            return new ModelAndView("redirect:/management/home");
        }
        return new ModelAndView("admin_and_staff/login");
    }

    @Override
    @GetMapping("/logout")
    public String logout(SecurityContextLogoutHandler securityContextLogoutHandler,  Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        customLogout(securityContextLogoutHandler, authentication, request, response);
        return "redirect:/management/login";
    }

    @Override
    @GetMapping("/forgotpassword")
    public ModelAndView forgotPassword() {
        return new ModelAndView("admin_and_staff/forgot-password");
    }

    @Override
    @GetMapping("/changepassword")
    public ModelAndView changePassword(
        @RequestParam(name = "auth") String auth
    ) {
        String email = getEmailFromAuthToken(auth);
        return (email!=null  && isExistEmail(email)) ?
                new ModelAndView("admin_and_staff/change-password") : new ModelAndView("redirect:/error");
    }

    @PostMapping("/isvalidemail")
    @ResponseBody
    public ResponseEntity<String> isValidEmail(@RequestParam(name = "email") String email) {
        email = email.trim();
        Matcher matcher = Constants.VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        if(matcher.matches()) {
            return isExistEmail(email) ? ResponseEntity.ok("existed") : ResponseEntity.ok("notExist");
        } else {
            return ResponseEntity.ok("unmatched");
        }
    }

    @PostMapping("/isvalidsodienthoai")
    @ResponseBody
    public ResponseEntity<String> isValidSoDienThoai(@RequestParam(name = "sodienthoai") String soDienThoai) {
        soDienThoai = soDienThoai.trim();
        Matcher matcher = Constants.VALID_SODIENTHOAI_REGEX.matcher(soDienThoai);
        if(matcher.matches()) {
            return nhanVienRepository.findNhanVienBySoDienThoai(soDienThoai).isPresent() ?
                    ResponseEntity.ok(Constants.DATA_EXISTED) : ResponseEntity.ok(Constants.DATA_NOT_EXIST);
        } else {
            return ResponseEntity.ok(Constants.DATA_PATTERN_UNMATCHED);
        }
    }

    @Override
    @PostMapping("/auth")
    @ResponseBody
    public ResponseEntity<String> sendChangePasswordEmail(
        @RequestParam(name = "email") String email
    ) {
        return super.sendChangePasswordEmail(email);
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
                String email = signinDataDto.getUserName();
                String password = Common.sha256Hash(signinDataDto.getPassword());
                boolean rememberMe = signinDataDto.isRememberMe();
                UserDetails nhanVienUserDetails = UserService.builder()
                        .nhanVienRepository(nhanVienRepository).build().loadNhanVienByEmail(email);
                if(password.equals(nhanVienUserDetails.getPassword()) && nhanVienUserDetails.isEnabled()) {
                    Cookie jwtCookie = new Cookie(Constants.JWT_COOKIE_NAME, jwtService.generateToken((NhanVienUserDetails) nhanVienUserDetails));
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

    @Override
    @PostMapping("/processforgotpassword")
    public ModelAndView processForgotPassword(
            @RequestParam(name = "auth") String auth,
            @RequestParam(name = "new") String newPassword
    ) {
        String email = getEmailFromAuthToken(auth);
        if(email != null) {
            NhanVien foundNhanVien = nhanVienRepository.findNhanVienByEmail(email).orElse(null);
            if(foundNhanVien!=null) {
                foundNhanVien.setMatKhau(Common.sha256Hash(newPassword));
                nhanVienRepository.save(foundNhanVien);
                return new ModelAndView("redirect:/management/login");
            }
        }
        log.error("email not found or invalid");
        return new ModelAndView("redirect:/error");
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

    @Override
    protected boolean isExistEmail(String email) {
        return nhanVienRepository.findNhanVienByEmail(email).filter(nhanVien -> nhanVien.getFlagDel()==0).isPresent();
    }

}
