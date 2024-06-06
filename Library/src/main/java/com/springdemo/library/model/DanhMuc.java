package com.springdemo.library.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
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
    @Setter(AccessLevel.NONE)
    @Column(name = "DateCreated")
    private Date dateCreated;
    @Column(name = "DateUpdated")
    private Date dateUpdated;

    @OneToMany(orphanRemoval = true)
    private List<TheLoai> theLoaiList;

    @Builder
    public DanhMuc(String tenDanhMuc, Date dateCreated) {
        this.tenDanhMuc = tenDanhMuc;
        this.dateCreated = dateCreated;
    }
}
