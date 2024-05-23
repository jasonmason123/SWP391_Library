package com.springdemo.library.model.other;

import com.springdemo.library.model.Sach;
<<<<<<< HEAD
import com.springdemo.library.model.User;
import com.springdemo.library.model.YeuCauMuonSach;
import com.springdemo.library.model.other.compositekeys.MuonSachCompositeKey;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

=======
import com.springdemo.library.model.YeuCauMuonSach;
import com.springdemo.library.model.other.compositekeys.SachDuocMuonCompositeKey;
import jakarta.persistence.*;
import lombok.*;

>>>>>>> 4a841c4b5e07ad8d326f0819f1b4fbff3ac487b7
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "SachDuocMuon")
<<<<<<< HEAD
@IdClass(MuonSachCompositeKey.class)
=======
@IdClass(SachDuocMuonCompositeKey.class)
>>>>>>> 4a841c4b5e07ad8d326f0819f1b4fbff3ac487b7
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
    @Column(name = "YeuCauId")
    private int soLuongMuon;

    @Builder
    public SachDuocMuon(Sach sach, YeuCauMuonSach yeuCauMuonSach, int soLuongMuon) {
        this.sach = sach;
        this.yeuCauMuonSach = yeuCauMuonSach;
        this.soLuongMuon = soLuongMuon;
    }
}
