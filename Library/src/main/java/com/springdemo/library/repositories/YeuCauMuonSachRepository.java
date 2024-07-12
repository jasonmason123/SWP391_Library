package com.springdemo.library.repositories;

import com.springdemo.library.model.YeuCauMuonSach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface YeuCauMuonSachRepository extends JpaRepository<YeuCauMuonSach, Long> {
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



}
