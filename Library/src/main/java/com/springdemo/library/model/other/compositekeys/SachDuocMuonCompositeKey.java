package com.springdemo.library.model.other.compositekeys;

import com.springdemo.library.model.Sach;



import com.springdemo.library.model.YeuCauMuonSach;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SachDuocMuonCompositeKey implements Serializable {
    private Sach sach;



    private YeuCauMuonSach yeuCauMuonSach;


    @Override
    public int hashCode() {
        return Objects.hash(sach, yeuCauMuonSach);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SachDuocMuonCompositeKey that = (SachDuocMuonCompositeKey) o;
        return Objects.equals(sach, that.sach) && Objects.equals(yeuCauMuonSach, that.yeuCauMuonSach);
    }
}
