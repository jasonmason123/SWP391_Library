package com.springdemo.library.controller;

import com.springdemo.library.model.dto.EmailDetailsDto;
import com.springdemo.library.model.dto.OtpDto;
import com.springdemo.library.model.dto.SigninDataDto;
import com.springdemo.library.repositories.NhanVienRepository;
import com.springdemo.library.repositories.UserRepository;
import com.springdemo.library.services.EmailService;
import com.springdemo.library.services.JwtService;
import com.springdemo.library.utils.Common;
import com.springdemo.library.utils.Constants;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@AllArgsConstructor
public abstract class AbstractAuthenticationController {
    protected JwtService jwtService;
    protected EmailService emailService;

    public abstract ModelAndView login(Authentication authentication);
    public abstract String logout(SecurityContextLogoutHandler securityContextLogoutHandler, Authentication authentication, HttpServletRequest request, HttpServletResponse response);
    public abstract ModelAndView forgotPassword();
    public abstract ModelAndView changePassword(String auth);
    public abstract ResponseEntity<String> processLogin(SigninDataDto signinDataDto, HttpServletResponse response);
    public abstract ModelAndView processForgotPassword(String auth, String newPassword);
    protected abstract boolean isExistEmail(String email);

    protected String getEmailFromAuthToken(String emailToken) {
        return jwtService.validateToken(emailToken) ? jwtService.getSubjectFromJWT(emailToken) : null;
    }

//Utils_________________________________________________________________________________________________________________
    public ResponseEntity<String> sendChangePasswordEmail(String email) {
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

    protected void customLogout(SecurityContextLogoutHandler securityContextLogoutHandler, Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        Cookie tokenCookie = Common.getCookie(request, Constants.JWT_COOKIE_NAME);
        if(tokenCookie != null) {
            tokenCookie.setMaxAge(0);
            tokenCookie.setPath("/");
            response.addCookie(tokenCookie);
            log.warn("Token cookie deleted");
        }
        securityContextLogoutHandler.logout(request, response, authentication);
        SecurityContextHolder.clearContext();
        log.warn("Security context cleared");
    }

    public ResponseEntity<String> sendOtp(
            String receiver,
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

}
