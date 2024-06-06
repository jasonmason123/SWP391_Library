package com.springdemo.library.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "`User`")
public class User {
    @Id
    @Setter(AccessLevel.NONE)
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    @Column(name = "TenUser", unique = true)
    private String tenUser;
    @Column(name = "MatKhau")
    private String matKhau;
    @Column(name = "Email", unique = true)
    private String email;
    @Column(name = "AvatarLink")
    private String avatarLink;
    @Column(name = "SoDienThoai", unique = true)
    private String soDienThoai;
    @Column(name = "SoCCCD", unique = true)
    @Setter(AccessLevel.NONE)
    private String soCCCD;
    @Column(name = "FlagDel")
    private int flagDel; //0: Hoat dong, 1: Vo hieu hoa
    @Setter(AccessLevel.NONE)
    @Column(name = "DateCreated")
    private Date dateCreated;
    @Column(name = "DateUpdated")
    private Date dateUpdated;

    @OneToMany(orphanRemoval = true)
    private List<Blog> blogList;
    @OneToMany(orphanRemoval = true)
    private List<BinhLuanSach> binhLuanSachList;
    @OneToMany(orphanRemoval = true)
    private List<BinhLuanBlog> binhLuanBlogList;
    @OneToMany(orphanRemoval = true)
    private List<YeuCauMuonSach> yeuCauMuonSachList;
    @Builder
    public User(String tenUser, String email, String avatarLink, String soDienThoai, String soCCCD, Date dateCreated) {
        this.tenUser = tenUser;
        this.email = email;
        this.avatarLink = (avatarLink!=null && !avatarLink.isEmpty()) ? avatarLink : "";
        this.soDienThoai = soDienThoai;
        this.soCCCD = soCCCD;
        this.flagDel = 0;
        this.dateCreated = dateCreated;
    }
}
