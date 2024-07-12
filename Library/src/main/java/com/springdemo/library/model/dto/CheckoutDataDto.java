package com.springdemo.library.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
import java.util.Map;

@AllArgsConstructor
@Getter
public class CheckoutDataDto {
    private Map<Integer, Double> clientCart;
    private Date ngayMuon;
    private Date ngayTra;
    private String diaChiNhanSach;
}
