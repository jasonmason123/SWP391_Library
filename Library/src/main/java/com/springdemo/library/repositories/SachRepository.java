package com.springdemo.library.repositories;
import com.springdemo.library.model.Sach;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SachRepository extends JpaRepository<Sach, Integer> {
    @Query("""
            SELECT s FROM Sach s JOIN s.sachDuocMuonList sd
            GROUP BY s.Id, s.tenSach, s.danhGia, s.tacGia, s.nhaXuatBan, s.soLuotDanhGia,
            s.soLuongTrongKho, s.dateCreated, s.dateUpdated, s.flagDel, s.giaTien, s.linkAnh, s.moTa
            ORDER BY SUM(sd.soLuongMuon) DESC""")
    List<Sach> findTopMostBorrowedBooks();

    @Query(value = "SELECT TOP 10 s.* FROM Sach s ORDER BY s.DateCreated DESC", nativeQuery = true)
    List<Sach> findTop10NewestBooks();

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
    @Query("SELECT s FROM Sach s JOIN s.theLoaiList t WHERE t.danhMuc.Id=:danhMucId")
    List<Sach> findSachByDanhMucId(@Param("danhMucId") int danhMucId);
    @Query(value = """
            select Sach.* from Sach join TagTheLoai on Sach.Id=TagTheLoai.SachId
            join TheLoai on TagTheLoai.TheLoaiId=TheLoai.Id join DanhMuc on TheLoai.DanhMucId=DanhMuc.Id
            where DanhMuc.tenDanhMuc=:tenDanhMuc AND TheLoai.TenTheLoai=:tenTheLoai""",nativeQuery = true)
    List<Sach> findSachByDanhMucVaTheLoai(@Param("tenDanhMuc") String tenDanhMuc,@Param("tenTheLoai") String tenTheLoai);
    @Query(value = "select s.*,TagTheLoai.TheLoaiId from Sach s join TagTheLoai on s.Id=TagTheLoai.SachId where s.Id=:Id",nativeQuery = true)
    Optional<Sach> findByIdWithGenres(@Param("Id") int Id);
}
