package com.springdemo.library.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "DanhMuc")
public class DanhMuc {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    @Column(name = "TenDanhMuc")
    private String tenDanhMuc;

    @OneToMany(orphanRemoval = true)
    private List<TheLoai> theLoaiList;

    @Builder
    public DanhMuc(String tenDanhMuc) {
        this.tenDanhMuc = tenDanhMuc;
    }
}
