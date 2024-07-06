package com.springdemo.library.repositories;

import com.springdemo.library.model.DanhMuc;
import com.springdemo.library.model.Sach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SachRepository extends JpaRepository<Sach, Integer> {
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
}
