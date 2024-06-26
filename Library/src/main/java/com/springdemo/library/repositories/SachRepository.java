package com.springdemo.library.repositories;

import com.springdemo.library.model.Sach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SachRepository extends JpaRepository<Sach, Integer> {
    @Query(value = "SELECT TOP 7 s.nhaXuatBan FROM Sach s GROUP BY s.nhaXuatBan", nativeQuery = true)
    List<String> findTopNhaXuatBan();

    @Query(value = """
            SELECT TOP 7 s.nhaXuatBan FROM Sach s JOIN TagTheLoai t ON s.Id = t.SachId
            WHERE t.TheLoaiId = :theLoaiId GROUP BY s.nhaXuatBan""", nativeQuery = true)
    List<String> findTopNhaXuatBanByTheLoaiId(@Param("theLoaiId") int theLoaiId);

    @Query(value = "SELECT TOP 7 s.tacGia FROM Sach s GROUP BY s.tacGia", nativeQuery = true)
    List<String> findTopTacGia();

    @Query(value = """
            SELECT TOP 7 s.tacGia FROM Sach s JOIN TagTheLoai t ON s.Id = t.SachId
            WHERE t.TheLoaiId = :theLoaiId GROUP BY s.tacGia""", nativeQuery = true)
    List<String> findTopTacGiaByTheLoai(@Param("theLoaiId") int theLoaiId);

    @Query(value = "SELECT DISTINCT TOP 5 s.* FROM Sach s JOIN TagTheLoai t ON s.Id = t.SachId WHERE t.TheLoaiId IN :theLoaiIdList", nativeQuery = true)
    List<Sach> findSachByListOfTheLoai(@Param("theLoaiIdList") List<Integer> theLoaiIdList);

    @Query("SELECT s FROM Sach s JOIN s.theLoaiList t WHERE t.Id=:theLoaiId")
    List<Sach> findSachByTheLoaiId(@Param("theLoaiId") int theLoaiId);
}
