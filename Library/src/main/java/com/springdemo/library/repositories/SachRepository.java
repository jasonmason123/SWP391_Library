package com.springdemo.library.repositories;
import com.springdemo.library.model.Sach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

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
    @Query(value = "select Sach.Id,Sach.tenSach,Sach.moTa,Sach.giaTien,Sach.danhGia,Sach.soLuongTrongKho,Sach.linkAnh,Sach.tacGia,Sach.nhaXuatBan,Sach.flagDel,Sach.dateCreated,Sach.dateUpdated \n" +
            "from Sach join TagTheLoai on Sach.Id=TagTheLoai.SachId join TheLoai on TagTheLoai.TheLoaiId=TheLoai.Id join DanhMuc on TheLoai.DanhMucId=DanhMuc.Id\n" +
            "where DanhMuc.tenDanhMuc=:tenDanhMuc",nativeQuery = true)
    List<Sach> findSachByDanhMuc(@Param("tenDanhMuc") String tenDanhMuc);
    @Query(value = "select Sach.Id,Sach.tenSach,Sach.moTa,Sach.giaTien,Sach.danhGia,Sach.soLuongTrongKho,Sach.linkAnh,Sach.tacGia,Sach.nhaXuatBan,Sach.flagDel,Sach.dateCreated,Sach.dateUpdated  \n" +
            "            from Sach join TagTheLoai on Sach.Id=TagTheLoai.SachId join TheLoai on TagTheLoai.TheLoaiId=TheLoai.Id where TheLoai.TenTheLoai=:tenTheLoai",nativeQuery = true)
    List<Sach> findSachByTheLoai(@Param("tenTheLoai") String tenTheLoai);
    @Query(value = "select Sach.Id,Sach.tenSach,Sach.moTa,Sach.giaTien,Sach.danhGia,Sach.soLuongTrongKho,Sach.linkAnh,Sach.tacGia,Sach.nhaXuatBan,Sach.flagDel,Sach.dateCreated,Sach.dateUpdated \n" +
            "from Sach join TagTheLoai on Sach.Id=TagTheLoai.SachId join TheLoai on TagTheLoai.TheLoaiId=TheLoai.Id join DanhMuc on TheLoai.DanhMucId=DanhMuc.Id\n" +
            "where DanhMuc.tenDanhMuc=:tenDanhMuc AND TheLoai.TenTheLoai=:tenTheLoai",nativeQuery = true)
    List<Sach> findSachByDanhMucVaTheLoai(@Param("tenDanhMuc") String tenDanhMuc,@Param("tenTheLoai") String tenTheLoai);
    @Query(value = "select s.*,TagTheLoai.TheLoaiId from Sach s join TagTheLoai on s.Id=TagTheLoai.SachId where s.Id=:Id",nativeQuery = true)
    Optional<Sach> findByIdWithGenres(@Param("Id") int Id);
    @Query(value="select SUM(SoLuong) from SachDuocMuon",nativeQuery = true)
    long countSachMuon();
    @Query(value="SELECT d.TenDanhMuc, COUNT(*) FROM Sach s JOIN TagTheLoai t on s.Id=t.SachId join TheLoai th on t.TheLoaiId=th.Id join DanhMuc d on th.DanhMucId=d.Id group by d.TenDanhMuc",nativeQuery = true)
    List<Object[]> countBooksByCategory();
    @Query(value="select SUM(SoLuongTrongKho) from Sach s",nativeQuery = true)
    long countSoLuongTrongKho();
}
