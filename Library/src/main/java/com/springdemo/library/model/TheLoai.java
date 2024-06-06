package com.springdemo.library.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "TheLoai")
public class TheLoai {
    @Id
    @Setter(AccessLevel.NONE)
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    @Column(name = "TenTheLoai")
    private String tenTheLoai;
    @ManyToOne
    @JoinColumn(name = "DanhMucId")
    private DanhMuc danhMuc;
    @Setter(AccessLevel.NONE)
    @Column(name = "DateCreated")
    private Date dateCreated;
    @Column(name = "DateUpdated")
    private Date dateUpdated;

    @ManyToMany(mappedBy = "theLoaiList")
    private List<Sach> sachList;

    @Builder
    public TheLoai(String tenTheLoai, DanhMuc danhMuc, Date dateCreated) {
        this.tenTheLoai = tenTheLoai;
        this.danhMuc = danhMuc;
        this.dateCreated = dateCreated;
    }
}
