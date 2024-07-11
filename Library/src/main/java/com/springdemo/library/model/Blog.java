package com.springdemo.library.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Blog")
public class Blog {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    @ManyToOne
    @JoinColumn(name = "TacGiaId")
    private User tacGia;
    @Column(name = "TieuDe")
    private String tieuDe;
    @Column(name = "NoiDung")
    private String noiDung;
    @Column(name = "DanhGia")
    private float danhGia;
    @Column(name = "SoLuotDanhGia")
    private int soLuotDanhGia;
    @Column(name = "NgayTao")
    private Date ngayTao;
    @Column(name = "NgayCapNhat")
    private Date ngayCapNhat;
    @Column(name = "FlagDel")
    private int flagDel; //-1: Nháp, 0: Hiện, 1: Ẩn, 2: Chờ duyệt

    @OneToMany(orphanRemoval = true, mappedBy = "blog")
    private List<BinhLuanBlog> binhLuanBlogList;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "BlogTag",
            joinColumns = @JoinColumn(name = "BlogId"),
            inverseJoinColumns = @JoinColumn(name = "TagId")
    )
    private List<Tag> tags;

    @Builder
    public Blog(User tacGia, String tieuDe, String noiDung, Date ngayTao, int flagDel) {
        this.tacGia = tacGia;
        this.tieuDe = tieuDe;
        this.noiDung = noiDung;
        this.danhGia = 0;
        this.soLuotDanhGia = 0;
        this.ngayTao = ngayTao;
        this.flagDel = flagDel;
    }
}