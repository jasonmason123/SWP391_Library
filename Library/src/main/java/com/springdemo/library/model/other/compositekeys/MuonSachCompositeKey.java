package com.springdemo.library.model.other.compositekeys;

import com.springdemo.library.model.Sach;
import com.springdemo.library.model.User;
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
public class MuonSachCompositeKey implements Serializable {
    private Sach sach;
    private User nguoiMuon;

    @Override
    public int hashCode() {
        return Objects.hash(sach, nguoiMuon);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MuonSachCompositeKey that = (MuonSachCompositeKey) o;
        return Objects.equals(sach, that.sach) && Objects.equals(nguoiMuon, that.nguoiMuon);
    }
}
