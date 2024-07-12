package com.springdemo.library.repositories;

import com.springdemo.library.model.NhanVien;
import com.springdemo.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NhanVienRepository extends JpaRepository<NhanVien, Integer> {

    @Query("SELECT n FROM NhanVien n WHERE  n.Id = :id AND n.email = :email")
    Optional<NhanVien> findNhanVienByIdAndEmail(@Param("id") int id , @Param("email") String email);

    @Query("SELECT n FROM NhanVien n WHERE n.tenNhanVien = :tenNhanVien")
    Optional<NhanVien> findNhanViensByTenNhanVien(@Param("tenNhanVien") String tenNhanVien);

    @Query("SELECT n FROM NhanVien n WHERE n.email = :email")
    Optional<NhanVien> findNhanVienByEmail(@Param("email") String email);

    @Query("SELECT n FROM NhanVien n WHERE n.soDienThoai = :soDienThoai")
    Optional<NhanVien> findNhanVienBySoDienThoai(@Param("soDienThoai") String soDienThoai);
    @Query("WITH AllMonths AS ( " +
            "SELECT 1 AS MonthNum " +
            "UNION ALL SELECT 2 " +
            "UNION ALL SELECT 3 " +
            "UNION ALL SELECT 4 " +
            "UNION ALL SELECT 5 " +
            "UNION ALL SELECT 6 " +
            "UNION ALL SELECT 7) " +
            "SELECT am.MonthNum AS Thang, " +
            "COUNT(n.dateCreated) AS SoLuongTao " +
            "FROM AllMonths am " +
            "LEFT JOIN NhanVien n ON am.MonthNum = MONTH(n.dateCreated) " +
            "GROUP BY am.MonthNum " +
            "ORDER BY am.MonthNum")
    List<Object[]> countStaffAccountByMonth();
}
