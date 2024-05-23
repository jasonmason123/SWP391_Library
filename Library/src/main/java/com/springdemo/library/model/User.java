package com.springdemo.library.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "User")
public class User {
    @Id
    @Setter(AccessLevel.NONE)
    @Column(name = "Id")
    private int Id;
    @Column(name = "TenUser")
    private String tenUser;
    @Column(name = "MatKhau")
    private String matKhau;
    @Column(name = "Email")
    private String email;
    @Column(name = "VaiTro")
    private String vaiTro; //1: admin, 0: customer, -1: staff
    @Column(name = "AvatarLink")
    private String avatarLink;
    @Column(name = "SoDienThoai")
    private String soDienThoai;
    @Column(name = "SoCCCD")
    @Setter(AccessLevel.NONE)
    private String soCCCD;
    @Column(name = "FlagDel")
    private int flagDel; //0: Hoat dong, 1: Vo hieu hoa

    @OneToMany(orphanRemoval = true)
    private List<Blog> blogList;
    @OneToMany(orphanRemoval = true)
    private List<BinhLuanSach> binhLuanSachList;
    @OneToMany(orphanRemoval = true)
    private List<BinhLuanBlog> binhLuanBlogList;
    @OneToMany(orphanRemoval = true)
    private List<YeuCauMuonSach> yeuCauMuonSachList;
    @Builder
    public User(String tenUser, String email, String vaiTro, String avatarLink, String soDienThoai, String soCCCD) {
        this.tenUser = tenUser;
        this.email = email;
        this.vaiTro = vaiTro;
        this.avatarLink = avatarLink;
        this.soDienThoai = soDienThoai;
        this.soCCCD = soCCCD;
        this.flagDel = 0;
    }
}
