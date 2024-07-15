package com.springdemo.library.model.dto;

import com.springdemo.library.model.YeuCauMuonSach;
import com.springdemo.library.model.other.SachDuocMuon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SachDuocMuonViewDto {
    private int sachId;
    private String tenSach;
    private double soTienDatCoc;
    private int daTra;

    public SachDuocMuonViewDto(SachDuocMuon sachDuocMuon) {
        sachId = sachDuocMuon.getSach().getId();
        tenSach = sachDuocMuon.getSach().getTenSach();
        soTienDatCoc = sachDuocMuon.getSoTienDatCoc();
        daTra = sachDuocMuon.getDaTra();
    }
}
