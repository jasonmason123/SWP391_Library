package com.springdemo.library.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SignupDataDto {
    private String tenUser;
    private String matKhau; //not hashed
    private String email;
    private String soDienThoai;
    private String soCCCD;
}
