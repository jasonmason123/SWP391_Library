package com.springdemo.library.model;

import com.springdemo.library.model.other.SachDuocMuon;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    private float danhGia;
    @Column(name = "GiaTien")
    private double giaTien;
    @Column(name = "SoLuongTrongKho")
    private int soLuongTrongKho;
    @Column(name = "SoLuotDanhGia")
    private int soLuotDanhGia;
    @Column(name = "LinkAnh")
    private String linkAnh;
    @Column(name = "FlagDel")
    private int flagDel;
    @Setter(AccessLevel.NONE)
    @Column(name = "DateCreated")
    private Date dateCreated;
    @Column(name = "DateUpdated")
    private Date dateUpdated;

    @OneToMany(orphanRemoval = true, mappedBy = "sach")
    private List<BinhLuanSach> binhLuan;
    @ManyToMany
    @JoinTable(
            name = "TagTheLoai",
            joinColumns = @JoinColumn(name = "SachId"),
            inverseJoinColumns = @JoinColumn(name = "TheLoaiId")
    )
    private List<TheLoai> theLoaiList;
    @OneToMany(orphanRemoval = true, mappedBy = "sach", cascade = CascadeType.ALL)
    private List<SachDuocMuon> sachDuocMuonList;

    @Builder
    public Sach(String tenSach,
                String tacGia,
                String nhaXuatBan,
                String moTa,
                double giaTien,
                int soLuongTrongKho,
                String linkAnh,
                int flagDel,
                Date dateCreated
               ) {
        this.tenSach = tenSach;
        this.tacGia = tacGia;
        this.nhaXuatBan = nhaXuatBan;
        this.moTa = moTa;
        this.danhGia = 0;
        this.giaTien = giaTien;
        this.soLuongTrongKho = soLuongTrongKho;
        this.soLuotDanhGia = 0;
        this.linkAnh = linkAnh;
        this.flagDel = flagDel;
        this.dateCreated = dateCreated;

    }

}
