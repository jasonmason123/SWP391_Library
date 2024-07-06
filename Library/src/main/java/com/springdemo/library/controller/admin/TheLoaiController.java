package com.springdemo.library.controller.admin;

import com.springdemo.library.model.*;
import com.springdemo.library.model.Sach;
import com.springdemo.library.model.dto.TheLoaiDto;
import com.springdemo.library.repositories.DanhMucRepository;
import com.springdemo.library.repositories.SachRepository;
import com.springdemo.library.repositories.TheLoaiRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

@Controller
@Slf4j
@AllArgsConstructor
@RequestMapping("/management")

public class TheLoaiController {


    private final DanhMucRepository danhMucRepository;
    private TheLoaiRepository TheLoaiRepository;
    private DanhMucRepository DanhMucRepository;

    @GetMapping("/genre")
    public ModelAndView viewGenre() {
        List<TheLoai> theLoaiList = TheLoaiRepository.findAll();
        List<DanhMuc> danhMucList = DanhMucRepository.findAll();
        ModelAndView manageBookViewModel = new ModelAndView("admin_and_staff/Layout");
        manageBookViewModel.addObject("includedPage", "admin_and_staff/theLoai");
        manageBookViewModel.addObject("title", "Quản lí Thể Loại");
        manageBookViewModel.addObject("modelClass", theLoaiList);
        manageBookViewModel.addObject("categories", danhMucList);

        return manageBookViewModel;
    }
    @PostMapping("/deleteGenre")
    @ResponseBody
    public ResponseEntity<String> deleteGenre(
            @RequestParam(name = "id") int id
    ) {
        try {
            TheLoai existedGenre = TheLoaiRepository.findById(id).orElse(null);
            if(existedGenre!=null) {

                TheLoaiRepository.save(existedGenre);
                TheLoaiRepository.deleteById(id);

                return ResponseEntity.ok().build();
            }
        } catch (DataIntegrityViolationException e) {
            log.error("Database error: " + e);
        }
        return ResponseEntity.badRequest().build();
    }
    @PostMapping("/addGenre")
    @ResponseBody
    public ResponseEntity<String> addGenre(
            @RequestBody TheLoaiDto theLoaiDto

    ) {
        try {

            TheLoai existedTheLoai = TheLoaiRepository.findById(theLoaiDto.getId()).orElse(null);
            DanhMuc existedDanhMuc=DanhMucRepository.findById(theLoaiDto.getDanhMucId()).orElse(null);
            if (existedTheLoai == null && existedDanhMuc != null) {
                log.warn("adding");
                TheLoaiRepository.save(
                        TheLoai.builder().tenTheLoai(theLoaiDto.getTenTheLoai())
                                .danhMuc(existedDanhMuc)
                                .dateCreated(new Date()).build()
                );

                log.warn("added");
                return ResponseEntity.ok().build();
            }
        } catch (DataIntegrityViolationException e) {
            log.error("Error: " + e);
        } catch (NullPointerException e) {
            log.error("System error: " + e);
        }
        log.warn("Cannot add");
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/updateGenre")
    @ResponseBody
    public ResponseEntity<String> updateGenre(
            @RequestParam(name = "id") int id,
            @RequestBody TheLoaiDto theLoaiDto
    ) {
        try {
            TheLoai existedTheLoai = TheLoaiRepository.findById(id).orElse(null);
            if (existedTheLoai != null) {
                DanhMuc existedDanhMuc = DanhMucRepository.findById(theLoaiDto.getDanhMucId()).orElse(null); // Tìm danh mục dựa trên ID mới
                if (existedDanhMuc == null) {
                    return ResponseEntity.badRequest().body("Danh mục không tồn tại");
                }

                String newName = (theLoaiDto.getTenTheLoai() != null && !theLoaiDto.getTenTheLoai().isBlank())
                        ? theLoaiDto.getTenTheLoai() : "";

                if (!newName.equals(existedTheLoai.getTenTheLoai())) {
                    existedTheLoai.setTenTheLoai(newName);
                }

                if (!existedDanhMuc.equals(existedTheLoai.getDanhMuc())) {
                    existedTheLoai.setDanhMuc(existedDanhMuc);
                }

                existedTheLoai.setDateUpdated(new Date());
                TheLoaiRepository.save(existedTheLoai);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.badRequest().body("Thể loại không tồn tại");
            }
        } catch (DataIntegrityViolationException e) {
            log.error("Error: " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi cập nhật thể loại");
        } catch (Exception e) {
            log.error("System error: " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi hệ thống");
        }
    }


}