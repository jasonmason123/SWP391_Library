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
    private String tenSach;
    private double soTienDatCoc;

    public SachDuocMuonViewDto(SachDuocMuon sachDuocMuon) {
        tenSach = sachDuocMuon.getSach().getTenSach();
        soTienDatCoc = sachDuocMuon.getSoTienDatCoc();
    }
}
