package com.springdemo.library.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SigninDataDto {
    private String userName;
    private String password;
    private boolean rememberMe;
}
