package com.springdemo.library.repositories;

import com.springdemo.library.model.NhanVien;
import com.springdemo.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NhanVienRepository extends JpaRepository<NhanVien, Integer> {

    @Query("SELECT n FROM NhanVien n WHERE  n.Id = :id AND n.tenNhanVien = :email")
    Optional<NhanVien> findNhanVienByIdAndEmail(@Param("id") int id , @Param("email") String email);

    @Query("SELECT n FROM NhanVien n WHERE n.tenNhanVien = :tenNhanVien")
    Optional<NhanVien> findNhanViensByTenNhanVien(@Param("tenNhanVien") String tenNhanVien);

    @Query("SELECT n FROM NhanVien n WHERE n.email = :email")
    Optional<NhanVien> findNhanVienByEmail(@Param("email") String email);

    @Query("SELECT n FROM NhanVien n WHERE n.soDienThoai = :soDienThoai")
    Optional<NhanVien> findNhanVienBySoDienThoai(@Param("soDienThoai") String soDienThoai);
}
