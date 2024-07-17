package com.springdemo.library.model.dto;

import com.springdemo.library.model.YeuCauMuonSach;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
@AllArgsConstructor
public class YeuCauMuonSachViewDto {
    private int Id;
    private Date ngayMuon;
    private Date ngayTra;
    private int quaHan;
    private double boiThuong;
    private int trangThai;
    private double soTienDatCoc;
    private double phiMuonSach;
    private String diaChiNhanSach;
    private Double phiVanChuyen;
    List<SachDuocMuonViewDto> sachDuocMuonList;

    public YeuCauMuonSachViewDto(YeuCauMuonSach yeuCauMuonSach) {
        this.Id = yeuCauMuonSach.getId();
        this.ngayMuon = yeuCauMuonSach.getNgayMuon();
        this.ngayTra = yeuCauMuonSach.getNgayTra();
        this.quaHan = yeuCauMuonSach.getQuaHan();
        this.boiThuong = yeuCauMuonSach.getBoiThuong();
        this.trangThai = yeuCauMuonSach.getTrangThai();
        this.soTienDatCoc = yeuCauMuonSach.getSoTienDatCoc();
        this.phiMuonSach = yeuCauMuonSach.getPhiMuonSach();
        this.diaChiNhanSach = yeuCauMuonSach.getDiaChiNhanSach();
        this.phiVanChuyen = yeuCauMuonSach.getPhiVanChuyen();
        this.sachDuocMuonList = yeuCauMuonSach.getSachDuocMuonList().stream().map(SachDuocMuonViewDto::new).toList();
    }
}
