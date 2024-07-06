package com.springdemo.library.model.dto;

import com.springdemo.library.model.DanhMuc;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
public class TheLoaiDto {
    private int id;
    private String tenTheLoai;
    private int danhMucId;
    private String tenDanhMuc;
    private DanhMuc danhMuc;

    @Builder
    public TheLoaiDto(int id,String tenTheLoai,int danhMucId,String tenDanhMuc,DanhMuc danhMuc) {
        this.tenTheLoai = tenTheLoai;
        this.id =id;
        this.danhMucId = danhMucId;
        this.tenDanhMuc = tenDanhMuc;
        this.danhMuc= danhMuc;
    }
}

