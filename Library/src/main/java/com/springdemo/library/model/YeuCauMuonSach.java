package com.springdemo.library.model;

import com.springdemo.library.model.other.SachDuocMuon;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "YeuCauMuonSach")
public class YeuCauMuonSach {
    @Id
    @Setter(AccessLevel.NONE)
    @Column(name = "Id")
    private int Id;
    @Column(name = "NgayMuon")
    private Date ngayMuon;
    @Column(name = "NgayTra")
    private Date ngayTra;
    @Column(name = "QuaHan")
    private int quaHan;
    @Column(name = "BoiThuong")
    private double boiThuong;
    @Column(name = "TrangThai")
    private int trangThai; //0:Chua duoc duyet, 1:Da duoc duyet, 2:Dang muon, 3:Da tra, -1:Tu choi
    @ManyToOne
    @JoinColumn(name = "NguoiMuonId")
    private User nguoiMuon;
    @Column(name = "DateCreated")
    private Date dateCreated;
    @Column(name = "DateUpdated")
    private Date dateUpdated;
    @Column(name = "SoTienDatCoc")
    private double soTienDatCoc;
    @OneToMany(mappedBy = "yeuCauMuonSach", orphanRemoval = true)
    List<SachDuocMuon> sachDuocMuonList;


    public YeuCauMuonSach(Date ngayMuon, Date ngayTra, User nguoiMuon, Date dateCreated) {
        this.ngayMuon = ngayMuon;
        this.ngayTra = ngayTra;
        this.nguoiMuon = nguoiMuon;
        this.quaHan = 0;
        this.boiThuong = 0;
        this.trangThai = 0;
        this.dateCreated = dateCreated;
    }
}
