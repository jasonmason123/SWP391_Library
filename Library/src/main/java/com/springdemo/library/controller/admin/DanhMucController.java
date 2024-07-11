package com.springdemo.library.controller.admin;

import com.springdemo.library.model.Blog;
import com.springdemo.library.model.DanhMuc;
import com.springdemo.library.model.Sach;
import com.springdemo.library.model.Sach;
import com.springdemo.library.repositories.DanhMucRepository;
import com.springdemo.library.repositories.SachRepository;
import com.springdemo.library.repositories.TheLoaiRepository;
import com.springdemo.library.services.GenerateViewService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

@Controller
@Slf4j
@AllArgsConstructor
@RequestMapping("/management/category")

public class DanhMucController {


    private DanhMucRepository DanhMucRepository;
    private GenerateViewService generateViewService;

    @GetMapping
    public ModelAndView viewCategory(Authentication authentication) {
        List<DanhMuc> danhMucList = DanhMucRepository.findAll();
        ModelAndView manageBookViewModel = generateViewService.generateStaffView("Quản lí Danh Mục", "admin_and_staff/danhMuc", authentication);
        manageBookViewModel.addObject("modelClass", danhMucList);
        return manageBookViewModel;
    }
    @PostMapping("/deleteCategory")
    @ResponseBody
    public ResponseEntity<String> deleteCategory(
            @RequestParam(name = "id") int id
    ) {
        try {
            DanhMuc existedCategory = DanhMucRepository.findById(id).orElse(null);
            if(existedCategory!=null) {
                DanhMucRepository.deleteById(id);
                return ResponseEntity.ok().build();
            }
        } catch (DataIntegrityViolationException e) {
            log.error("Database error: " + e);
        }
        return ResponseEntity.badRequest().build();
    }
    @PostMapping("/addCategory")
    @ResponseBody
    public ResponseEntity<String> addCategory(
            @RequestBody DanhMuc danhMucDto
    ) {
        try {
            DanhMuc existedDanhMuc = DanhMucRepository.findById(danhMucDto.getId()).orElse(null);
            if (existedDanhMuc == null) {
                log.warn("adding");
                DanhMucRepository.save(
                        DanhMuc.builder().tenDanhMuc(danhMucDto.getTenDanhMuc())

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

    @PostMapping("/updateCategory")
    @ResponseBody
    public ResponseEntity<String> updateCategory(
            @RequestParam(name = "id") int id,
            @RequestBody DanhMuc danhMucDto
    ) {
        try {
            DanhMuc existedDanhMuc = DanhMucRepository.findById(id).orElse(null);
            log.warn("Id:" + id);
            if (existedDanhMuc != null) {


                String newName = (danhMucDto.getTenDanhMuc()!=null && !danhMucDto.getTenDanhMuc().isBlank())
                        ? danhMucDto.getTenDanhMuc() : "";

                if(!newName.equals(existedDanhMuc.getTenDanhMuc())) {
                    existedDanhMuc.setTenDanhMuc(newName);
                }

                existedDanhMuc.setDateUpdated(new Date());
                DanhMucRepository.save(existedDanhMuc);
                return ResponseEntity.ok().build();
            }
        } catch (DataIntegrityViolationException e) {
            log.error("Error: " + e);
        } catch (NullPointerException e) {
            log.error("System error: " + e);
        }
        log.warn("Category not found");
        return ResponseEntity.badRequest().build();
    }
}