package com.springdemo.library.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private int Id;
    @Column(name = "TenTheLoai")
    private String tenTheLoai;
    @ManyToOne
    @JoinColumn(name = "DanhMucId")
    private DanhMuc danhMuc;

    @ManyToMany(mappedBy = "theLoaiList")
    private List<Sach> sachList;
}
