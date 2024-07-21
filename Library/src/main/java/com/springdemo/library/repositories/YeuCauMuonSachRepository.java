package com.springdemo.library.repositories;

import com.springdemo.library.model.YeuCauMuonSach;
import com.springdemo.library.model.dto.MissingSach;
import com.springdemo.library.model.other.SachDuocMuon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface YeuCauMuonSachRepository extends JpaRepository<YeuCauMuonSach, Integer> {
    @Query(value = "WITH AllMonths AS ( SELECT 1 AS MonthNum UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 " +
            "    UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9 " +
            "    UNION ALL SELECT 10 UNION ALL SELECT 11 UNION ALL SELECT 12 ), YearMonths AS ( " +
            "    SELECT MonthNum FROM AllMonths WHERE MonthNum BETWEEN 1 AND MONTH(GETDATE()) " +  // Adjust to the current month
            ") " +
            "SELECT ym.MonthNum AS Thang, COALESCE(COUNT(y.ngayMuon), 0) AS SoLuotMuon FROM YearMonths ym " +
            "LEFT JOIN YeuCauMuonSach y ON ym.MonthNum = MONTH(y.ngayMuon) GROUP BY ym.MonthNum ORDER BY ym.MonthNum",
            nativeQuery = true)
    List<Object[]> countLoansByMonth();
    @Query(value = "SELECT y.* FROM YeuCauMuonSach y WHERE (y.NgayTra - GETDATE()) = 3", nativeQuery = true)
    List<YeuCauMuonSach> findYeuCauWhereDueDateIsIn3Days();

    @Query(value = "SELECT y.* FROM YeuCauMuonSach y WHERE y.NgayTra = GETDATE()", nativeQuery = true)
    List<YeuCauMuonSach> findYeuCauWhereDueDateIsToday();

    @Query("SELECT new com.springdemo.library.model.dto.MissingSach(s.sach, COUNT(s.sach)) FROM YeuCauMuonSach y JOIN y.sachDuocMuonList s WHERE s.trangThai = -1 GROUP BY s.sach")
    List<MissingSach> findAllMissingSach();

    @Query("SELECT s FROM YeuCauMuonSach y JOIN y.sachDuocMuonList s WHERE s.sach.Id = :sachId")
    List<SachDuocMuon> findAllSachDuocMuonBySachId(@Param("sachId") int sachId);

    @Query(value = "SELECT y.* FROM YeuCauMuonSach y WHERE y.NgayTra < GETDATE()", nativeQuery = true)
    List<YeuCauMuonSach> findAllOverdueYeuCau();
    @Query("SELECT COUNT(y.Id) FROM YeuCauMuonSach y WHERE y.trangThai = 0")
    long countPendingYeuCauMuonSach();
    @Query(value="select COALESCE(SUM(BoiThuong), 0) from YeuCauMuonSach",nativeQuery = true)
    long countBoiThuong();
    @Query(value="select COALESCE(SUM(SoTienDatCoc), 0) from YeuCauMuonSach where TrangThai=2", nativeQuery = true)
    long countSoTienDatCoc();
    @Query(value="select COALESCE(SUM(PhiMuonSach), 0) from YeuCauMuonSach where TrangThai=2 or TrangThai=3",nativeQuery = true)
    long countPhiMuonSach();

    @Query("SELECT y FROM YeuCauMuonSach y ORDER BY y.trangThai, y.dateCreated")
    List<YeuCauMuonSach> findAllYeuCauOrderByDateCreated();

    @Query("SELECT y FROM YeuCauMuonSach y WHERE y.nguoiMuon.Id = :userId AND y.trangThai = :trangThai ORDER BY y.ngayTra DESC")
    List<YeuCauMuonSach> findAllByNguoiMuonAndTrangThaiOrderByNgayTraDesc(@Param("userId") int userId, @Param("trangThai") int trangThai);

    @Query("SELECT y FROM YeuCauMuonSach y WHERE y.nguoiMuon.Id = :userId AND y.trangThai = :trangThai ORDER BY y.ngayTra")
    List<YeuCauMuonSach> findAllByNguoiMuonAndTrangThai(@Param("userId") int userId, @Param("trangThai") int trangThai);
}