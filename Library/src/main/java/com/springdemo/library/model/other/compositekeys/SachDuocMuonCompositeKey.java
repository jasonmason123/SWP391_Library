package com.springdemo.library.model.other.compositekeys;

import com.springdemo.library.model.Sach;
<<<<<<< HEAD:Library/src/main/java/com/springdemo/library/model/other/compositekeys/MuonSachCompositeKey.java
import com.springdemo.library.model.User;
=======
>>>>>>> 4a841c4b5e07ad8d326f0819f1b4fbff3ac487b7:Library/src/main/java/com/springdemo/library/model/other/compositekeys/SachDuocMuonCompositeKey.java
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
<<<<<<< HEAD:Library/src/main/java/com/springdemo/library/model/other/compositekeys/MuonSachCompositeKey.java
    private YeuCauMuonSach nguoiMuon;
=======
    private YeuCauMuonSach yeuCauMuonSach;
>>>>>>> 4a841c4b5e07ad8d326f0819f1b4fbff3ac487b7:Library/src/main/java/com/springdemo/library/model/other/compositekeys/SachDuocMuonCompositeKey.java

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
