package com.springdemo.library.model.other;

import com.springdemo.library.model.Sach;
import com.springdemo.library.model.User;
import com.springdemo.library.model.other.compositekeys.MuonSachCompositeKey;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "MuonSach")
@IdClass(MuonSachCompositeKey.class)
public class MuonSach {
    @Id
    @ManyToOne
    @JoinColumn(name = "SachId")
    @Setter(AccessLevel.NONE)
    private Sach sach;
    @Id
    @ManyToOne
    @JoinColumn(name = "NguoiMuonId")
    @Setter(AccessLevel.NONE)
    private User nguoiMuon;
    @Column(name = "SoLuongMuon")
    private int soLuongMuon;
    @Column(name = "NgayMuon")
    @Setter(AccessLevel.NONE)
    private Date ngayMuon;
    @Column(name = "NgayTra")
    @Setter(AccessLevel.NONE)
    private Date ngayTra;
    @Column(name = "QuaHan")
    @Setter(AccessLevel.NONE)
    private int quaHan;
    @Column(name = "BoiThuong")
    private double boiThuong;
    @Column(name = "TrangThaiXetDuyet")
    private int trangThaiXetDuyet;

    @Builder
    public MuonSach(Sach sach, User nguoiMuon, int soLuongMuon, Date ngayMuon, Date ngayTra) {
        this.sach = sach;
        this.nguoiMuon = nguoiMuon;
        this.soLuongMuon = soLuongMuon;
        this.ngayMuon = ngayMuon;
        this.ngayTra = ngayTra;
        this.quaHan = 0;
        this.boiThuong = 0;
        this.trangThaiXetDuyet = 0;
    }
}
