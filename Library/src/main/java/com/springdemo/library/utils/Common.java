package com.springdemo.library.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Logger;

public class Common {

    private static final Logger logger = Logger.getLogger(Common.class.getName());
    private static final Random random = new Random();

    public static String sha256Hash(String password) {
        try {
            // Create a MessageDigest instance for SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Add password bytes to digest
            md.update(password.getBytes());

            // Get the hashed bytes
            byte[] hashedBytes = md.digest();

            // Convert hashed bytes to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }

            // Return the hexadecimal representation of the hashed password
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            logger.warning(e.toString());
            return null;
        }
    }

    public static String generateRandomNumberString(int length) {
        StringBuilder result = new StringBuilder(length);
        for(int i=0; i<length; i++) {
            int index = random.nextInt(9);
            result.append(index);
        }
        return result.toString();
    }

    public static Cookie getCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            return Arrays.stream(cookies).filter(cookie -> cookie.getName().equals(cookieName)).findFirst().orElse(null);
        }
        return null;
    }

    public static boolean isAuthenticated(Authentication authentication) {
        return authentication!=null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken);
    }
}
