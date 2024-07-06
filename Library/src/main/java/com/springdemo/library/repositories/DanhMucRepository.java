package com.springdemo.library.repositories;

import com.springdemo.library.model.DanhMuc;
import com.springdemo.library.model.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DanhMucRepository extends JpaRepository<DanhMuc, Integer> {

    @Query("SELECT n FROM DanhMuc n WHERE n.tenDanhMuc = :tenDanhMuc")
    List<DanhMuc> findDanhMucByName(@Param("tenDanhMuc") String tenDanhMuc);
}
