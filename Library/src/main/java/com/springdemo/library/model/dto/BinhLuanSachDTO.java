package com.springdemo.library.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BinhLuanSachDTO {
    private int sachId;
    private int danhGia;
    private String noiDung;
}
