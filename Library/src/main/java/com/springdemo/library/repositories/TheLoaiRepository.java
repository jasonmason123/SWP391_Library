package com.springdemo.library.repositories;

import com.springdemo.library.model.TheLoai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TheLoaiRepository extends JpaRepository<TheLoai, Integer> {
    @Query("SELECT n FROM TheLoai n WHERE n.danhMuc.Id = :danhMucId")
    List<TheLoai> findTheLoaiByDanhMucId(@Param("danhMucId") int danhMucId);
    @Query(value = "select TagTheLoai.TheLoaiId from Sach join TagTheLoai on Sach.Id=TagTheLoai.SachId where Sach.Id=:Id",nativeQuery = true)
   List<Integer> findTagTheLoai(@Param("Id") int Id);
}
