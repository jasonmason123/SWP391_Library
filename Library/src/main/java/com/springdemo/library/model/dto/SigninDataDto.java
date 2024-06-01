package com.springdemo.library.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SigninDataDto {
    private String userName;
    private String password;
    private boolean rememberMe;
}
