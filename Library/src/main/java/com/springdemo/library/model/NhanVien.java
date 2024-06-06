package com.springdemo.library.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "NhanVien")
public class NhanVien {
    @Id
    @Setter(AccessLevel.NONE)
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    @Column(name = "TenNhanVien")
    private String tenNhanVien;
    @Column(name = "MatKhau")
    private String matKhau;
    @Column(name = "Email", unique = true)
    private String email;
    @Column(name = "SoDienThoai")
    private String soDienThoai;
    @Column(name = "DiaChi")
    private String diaChi;
    @Column(name = "FlagDel")
    private int flagDel;
    @Column(name = "VaiTro")
    private String vaiTro; //0: admin, 1: staff
    @Setter(AccessLevel.NONE)
    @Column(name = "DateCreated")
    private Date dateCreated;
    @Column(name = "DateUpdated")
    private Date dateUpdated;

    @Builder
    public NhanVien(String tenNhanVien, String matKhau, String email, String soDienThoai, String diaChi, String vaiTro, Date dateCreated) {
        this.tenNhanVien = tenNhanVien;
        this.matKhau = matKhau;
        this.email = email;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
        this.vaiTro = vaiTro;
        this.flagDel = 0;
        this.dateCreated = dateCreated;
    }
}
