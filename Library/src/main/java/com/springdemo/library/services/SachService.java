package com.springdemo.library.services;

import com.springdemo.library.model.Sach;
import com.springdemo.library.model.TheLoai;
import com.springdemo.library.repositories.SachRepository;
import com.springdemo.library.repositories.TheLoaiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class SachService {
    @Autowired
    private SachRepository sachRepository;

    @Autowired
    private TheLoaiRepository theLoaiRepository;

    public Sach addSach(Sach sach, List<Integer> theLoaiIds) {
        List<TheLoai> theLoaiList = theLoaiRepository.findAllById(theLoaiIds);
        sach.setTheLoaiList(theLoaiList);
        return sachRepository.save(sach);
    }
    public Sach getSachById(int id) {
        return sachRepository.findById(id).orElse(null);
    }

    public void updateTheLoaiForSach(Sach sach, List<Integer> theLoaiIds) {
        List<TheLoai> theLoaiList = theLoaiRepository.findAllById(theLoaiIds);
        sach.setTheLoaiList(theLoaiList);
    }
    public List<TheLoai> findSelectedGenresForBook(int bookId) {
        Optional<Sach> bookOptional = sachRepository.findByIdWithGenres(bookId);
        if (bookOptional.isPresent()) {
            return bookOptional.get().getTheLoaiList();
        }
        return Collections.emptyList();
    }
}
