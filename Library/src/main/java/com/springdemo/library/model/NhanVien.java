package com.springdemo.library.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "NhanVien")
public class NhanVien {
    @jakarta.persistence.Id
    @Setter(AccessLevel.NONE)
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    @Column(name = "TenNhanVien", unique = true)
    private String tenNhanVien;
    @Column(name = "MatKhau")
    private String matKhau;
    @Column(name = "Email", unique = true)
    private String email;
    @Column(name = "FlagDel")
    private int flagDel;
    @Column(name = "VaiTro")
    private String vaiTro; //0: admin, 1: staff
}
