package com.springdemo.library.model.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Getter
@Setter
public class SachDto {

    private String tenSach;
    private String tacGia;
    private String nhaXuatBan;
    private String moTa;
    private int danhGia;
    private double giaTien;
    private int soLuongTrongKho;
    private String linkAnh;
    private int flagDel;
    private Date dateCreated;
    private List<Integer> theLoaiId;




}
