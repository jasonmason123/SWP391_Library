package com.springdemo.library.model.other;

import com.springdemo.library.model.Sach;


import com.springdemo.library.model.YeuCauMuonSach;
import com.springdemo.library.model.other.compositekeys.SachDuocMuonCompositeKey;
import jakarta.persistence.*;
import lombok.*;




@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "SachDuocMuon")

@IdClass(SachDuocMuonCompositeKey.class)



public class SachDuocMuon {
    @Id
    @ManyToOne
    @JoinColumn(name = "SachId")
    @Setter(AccessLevel.NONE)
    private Sach sach;
    @Id
    @ManyToOne
    @JoinColumn(name = "YeuCauId")
    @Setter(AccessLevel.NONE)
    private YeuCauMuonSach yeuCauMuonSach;
    @Column(name = "SoLuong")
    private int soLuongMuon;

    @Builder
    public SachDuocMuon(Sach sach, YeuCauMuonSach yeuCauMuonSach, int soLuongMuon) {
        this.sach = sach;
        this.yeuCauMuonSach = yeuCauMuonSach;
        this.soLuongMuon = soLuongMuon;
    }
}
