package com.springdemo.library.model.dto;

import lombok.Getter;

import java.util.Date;

@Getter
public class OtpDto {
    private String otp;
    private Date expiryDate;

    public OtpDto(String otp) {
        this.otp = otp;
        this.expiryDate = new Date((new Date()).getTime() + 2*60*1000);
    }
}
