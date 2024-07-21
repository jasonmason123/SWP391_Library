package com.springdemo.library.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class YeuCauMuonSachUpdateData {
    private int yeuCauId;
    private int status;
    private Double phiVanChuyen; //Phi van chuyen
    private List<Integer> sachDaTraList;
}
