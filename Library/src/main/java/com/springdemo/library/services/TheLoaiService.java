package com.springdemo.library.services;

import com.springdemo.library.model.DanhMuc;
import com.springdemo.library.model.TheLoai;
import com.springdemo.library.model.dto.TheLoaiDto;
import com.springdemo.library.repositories.DanhMucRepository;
import com.springdemo.library.repositories.TheLoaiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TheLoaiService {

    @Autowired
    private TheLoaiRepository theLoaiRepository;

    @Autowired
    private DanhMucRepository danhMucRepository;

    public void addGenre(TheLoaiDto theLoaiDto) {
        DanhMuc danhMuc = danhMucRepository.findById(theLoaiDto.getDanhMucId())
                .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại"));
        TheLoai theLoai = new TheLoai();
        theLoai.setTenTheLoai(theLoaiDto.getTenTheLoai());
        theLoai.setDanhMuc(danhMuc);
        theLoaiRepository.save(theLoai);
    }

    public void updateGenre(int id, TheLoaiDto theLoaiDto) {
        TheLoai theLoai = theLoaiRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Thể loại không tồn tại"));
        DanhMuc danhMuc = danhMucRepository.findById(theLoaiDto.getDanhMucId())
                .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại"));
        theLoai.setTenTheLoai(theLoaiDto.getTenTheLoai());
        theLoai.setDanhMuc(danhMuc);
        theLoaiRepository.save(theLoai);
    }

    public void deleteGenre(int id) {
        theLoaiRepository.deleteById(id);
    }
}
