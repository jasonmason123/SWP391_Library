package com.springdemo.library.repositories;

import com.springdemo.library.model.YeuCauMuonSach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface YeuCauMuonSachRepository extends JpaRepository<YeuCauMuonSach, Integer> {
    @Query(value="WITH AllMonths AS ( " +
            "SELECT 1 AS MonthNum " +
            "UNION ALL (SELECT 2) " +
            "UNION ALL (SELECT 3) " +
            "UNION ALL (SELECT 4) " +
            "UNION ALL (SELECT 5) " +
            "UNION ALL (SELECT 6) " +
            "UNION ALL (SELECT 7)) " +
            "(SELECT am.MonthNum AS Thang, " +
            "COUNT(y.ngayMuon) AS SoLuotMuon " +
            "FROM AllMonths am " +
            "LEFT JOIN YeuCauMuonSach y ON am.MonthNum = MONTH(y.ngayMuon) " +
            "GROUP BY am.MonthNum " +
            "ORDER BY am.MonthNum)")
    List<Object[]> countLoansByMonth();
    @Query(value = "SELECT y.* FROM YeuCauMuonSach y WHERE (y.NgayTra - GETDATE()) = 3", nativeQuery = true)
    List<YeuCauMuonSach> findYeuCauWhereDueDateIsIn3Days();

    @Query(value = "SELECT y.* FROM YeuCauMuonSach y WHERE y.NgayTra = GETDATE()", nativeQuery = true)
    List<YeuCauMuonSach> findYeuCauWhereDueDateIsToday();

    @Query(value = "SELECT y.* FROM YeuCauMuonSach y WHERE y.NgayTra < GETDATE()", nativeQuery = true)
    List<YeuCauMuonSach> findAllOverdueYeuCau();
    @Query(value="select SUM(BoiThuong) from YeuCauMuonSach",nativeQuery = true)
    long countBoiThuong();
    @Query(value="select SUM(SoTienDatCoc) from YeuCauMuonSach where TrangThai=2",nativeQuery = true)
    long countSoTienDatCoc();
    @Query(value="select SUM(PhiMuonSach) from YeuCauMuonSach",nativeQuery = true)
    long countPhiMuonSach();


}
