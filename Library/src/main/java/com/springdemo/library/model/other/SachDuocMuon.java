package com.springdemo.library.model.other;

import com.springdemo.library.model.Sach;


import com.springdemo.library.model.YeuCauMuonSach;
import com.springdemo.library.model.other.compositekeys.SachDuocMuonCompositeKey;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "SachDuocMuon")
@IdClass(SachDuocMuonCompositeKey.class)
public class SachDuocMuon {
    @Id
    @ManyToOne
    @Cascade(org.hibernate.annotations.CascadeType.PERSIST)
    @JoinColumn(name = "SachId")
    @Setter(AccessLevel.NONE)
    private Sach sach;
    @Id
    @ManyToOne
    @Cascade(org.hibernate.annotations.CascadeType.PERSIST)
    @JoinColumn(name = "YeuCauId")
    @Setter(AccessLevel.NONE)
    private YeuCauMuonSach yeuCauMuonSach;
    @Column(name = "SoTienDatCoc")
    private double soTienDatCoc;
    @Column(name = "TrangThai")
    private int trangThai; //0: Dang muon, 1: Da tra, -1: Da mat
    @Builder
    public SachDuocMuon(Sach sach, YeuCauMuonSach yeuCauMuonSach, double soTienDatCoc) {
        this.sach = sach;
        this.yeuCauMuonSach = yeuCauMuonSach;
        this.soTienDatCoc = soTienDatCoc;
        this.trangThai = 0;
    }
}
