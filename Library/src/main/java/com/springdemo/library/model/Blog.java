package com.springdemo.library.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
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
    private int danhGia;
    @Setter(AccessLevel.NONE)
    @Column(name = "NgayTao")
    private Date ngayTao;
    @Column(name = "FlagDel")
    private int flagDel;

    @OneToMany(orphanRemoval = true)
    private List<BinhLuanBlog> binhLuanBlogList;
    @ManyToMany
    @JoinTable(
            name = "BlogTag",
            joinColumns = @JoinColumn(name = "BlogId"),
            inverseJoinColumns = @JoinColumn(name = "TagId")
    )
    private List<Tag> tags;

    @Builder
    public Blog(User tacGia, String tieuDe, String noiDung, int danhGia, Date ngayTao) {
        this.tacGia = tacGia;
        this.tieuDe = tieuDe;
        this.noiDung = noiDung;
        this.danhGia = danhGia;
        this.ngayTao = ngayTao;
    }
}
