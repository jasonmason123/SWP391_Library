package com.springdemo.library.model;

import com.springdemo.library.model.other.SachDuocMuon;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

import java.util.ArrayList;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @OneToMany(mappedBy = "yeuCauMuonSach", orphanRemoval = true, cascade = CascadeType.ALL)
    List<SachDuocMuon> sachDuocMuonList;

    @Builder
    public YeuCauMuonSach(Date ngayMuon, Date ngayTra, User nguoiMuon, double soTienDatCoc, Date dateCreated) {
        this.ngayMuon = ngayMuon;
        this.ngayTra = ngayTra;
        this.nguoiMuon = nguoiMuon;
        this.soTienDatCoc = soTienDatCoc;
        this.quaHan = 0;
        this.boiThuong = 0;
        this.trangThai = 0;
        this.dateCreated = dateCreated;
    }
}
