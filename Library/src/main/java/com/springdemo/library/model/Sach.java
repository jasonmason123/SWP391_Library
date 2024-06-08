package com.springdemo.library.model;

import com.springdemo.library.model.other.SachDuocMuon;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Sach")
public class Sach {
    @Id
    @Setter(AccessLevel.NONE)
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    @Column(name = "TenSach")
    private String tenSach;
    @Column(name = "TacGia")
    private String tacGia;
    @Column(name = "NhaXuatBan")
    private String nhaXuatBan;
    @Column(name = "MoTa")
    private String moTa;
    @Column(name = "DanhGia")
    private int danhGia;
    @Column(name = "GiaTien")
    private double giaTien;
    @Column(name = "SoLuongTrongKho")
    private int soLuongTrongKho;
    @Column(name = "LinkAnh")
    private String linkAnh;
    @Column(name = "FlagDel")
    private int flagDel;
    @Setter(AccessLevel.NONE)
    @Column(name = "DateCreated")
    private Date dateCreated;
    @Column(name = "DateUpdated")
    private Date dateUpdated;

    @OneToMany(orphanRemoval = true)
    private List<BinhLuanSach> binhLuan;
    @ManyToMany
    @JoinTable(
            name = "TagTheLoai",
            joinColumns = @JoinColumn(name = "SachId"),
            inverseJoinColumns = @JoinColumn(name = "TheLoaiId")
    )
    private List<TheLoai> theLoaiList;
    @OneToMany(orphanRemoval = true)
    private List<SachDuocMuon> sachDuocMuonList;

    @Builder
    public Sach(String tenSach,
                String tacGia,
                String nhaXuatBan,
                String moTa,
                int danhGia,
                double giaTien,
                int soLuongTrongKho,
                String linkAnh,
                int flagDel,
                Date dateCreated) {
        this.tenSach = tenSach;
        this.tacGia = tacGia;
        this.nhaXuatBan = nhaXuatBan;
        this.moTa = moTa;
        this.danhGia = danhGia;
        this.giaTien = giaTien;
        this.soLuongTrongKho = soLuongTrongKho;
        this.linkAnh = linkAnh;
        this.flagDel = flagDel;
        this.dateCreated = dateCreated;
    }
}
