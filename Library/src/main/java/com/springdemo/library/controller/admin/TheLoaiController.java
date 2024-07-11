package com.springdemo.library.controller.admin;

import com.springdemo.library.model.*;
import com.springdemo.library.model.dto.TheLoaiDto;
import com.springdemo.library.repositories.DanhMucRepository;
import com.springdemo.library.repositories.TheLoaiRepository;
import com.springdemo.library.services.GenerateViewService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

@Controller
@Slf4j
@AllArgsConstructor
@RequestMapping("/management/genre")

public class TheLoaiController {

    private TheLoaiRepository theLoaiRepository;
    private DanhMucRepository danhMucRepository;
    private GenerateViewService generateViewService;

    @GetMapping
    public ModelAndView viewGenre(Authentication authentication) {
        List<TheLoai> theLoaiList = theLoaiRepository.findAll();
        List<DanhMuc> danhMucList = danhMucRepository.findAll();
        ModelAndView manageBookViewModel = generateViewService.generateStaffView("Quản lí Thể Loại", "admin_and_staff/theLoai", authentication);
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
            TheLoai existedGenre = theLoaiRepository.findById(id).orElse(null);
            if(existedGenre!=null) {
                theLoaiRepository.save(existedGenre);
                theLoaiRepository.deleteById(id);
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
            TheLoai existedTheLoai = theLoaiRepository.findById(theLoaiDto.getId()).orElse(null);
            DanhMuc existedDanhMuc= danhMucRepository.findById(theLoaiDto.getDanhMucId()).orElse(null);
            if (existedTheLoai == null && existedDanhMuc != null) {
                log.warn("adding");
                theLoaiRepository.save(
                        TheLoai.builder().tenTheLoai(theLoaiDto.getTenTheLoai())
                                .danhMuc(existedDanhMuc)
                                .dateCreated(new Date()).build()
                );
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
            TheLoai existedTheLoai = theLoaiRepository.findById(id).orElse(null);
            if (existedTheLoai != null) {
                DanhMuc existedDanhMuc = danhMucRepository.findById(theLoaiDto.getDanhMucId()).orElse(null); // Tìm danh mục dựa trên ID mới
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
                theLoaiRepository.save(existedTheLoai);
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