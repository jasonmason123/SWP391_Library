package com.springdemo.library.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BinhLuanDto {
    private int postId;
    private int danhGia;
    private String noiDung;
}
