package com.springdemo.library.controller.admin;

import com.springdemo.library.model.NhanVien;
import com.springdemo.library.repositories.NhanVienRepository;
import com.springdemo.library.utils.Common;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

@Controller
@Slf4j
@AllArgsConstructor
@RequestMapping("/management")

public class StaffManagementController {

    private NhanVienRepository nhanVienRepository;
    @GetMapping("/staff")
    public ModelAndView viewStaff() {
        List<NhanVien> nhanVienList = nhanVienRepository.findAll();
        ModelAndView manageStaffViewModel = new ModelAndView("admin_and_staff/Layout");
        manageStaffViewModel.addObject("includedPage","admin_and_staff/manageStaff");
        manageStaffViewModel.addObject("title","Quản lí Nhân viên");
        manageStaffViewModel.addObject("modelClass", nhanVienList);
        return manageStaffViewModel;
    } //first Spring Boot Code of TMinh

    @PostMapping("/addStaff")
    @ResponseBody
    public ResponseEntity<String> addStaff(
            @RequestBody NhanVien nhanVienDto
    ) {
        try {
            NhanVien existedNhanVien = nhanVienRepository.findNhanVienByEmail(nhanVienDto.getEmail()).orElse(null);
            if (existedNhanVien == null) {
                log.warn("adding");
                nhanVienRepository.save(
                        NhanVien.builder().tenNhanVien(nhanVienDto.getTenNhanVien())
                                .matKhau(Common.sha256Hash(nhanVienDto.getMatKhau()))
                                .email(nhanVienDto.getEmail())
                                .soDienThoai(nhanVienDto.getSoDienThoai())
                                .diaChi(nhanVienDto.getDiaChi())
                                .vaiTro(nhanVienDto.getVaiTro())
                                .dateCreated(new Date()).build()
                ); //Add sdt and diachi to front-end
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

    @PostMapping("/updateStaff")
    @ResponseBody
    public ResponseEntity<String> updateStaff(
            @RequestParam(name = "id") int id,
            @RequestBody NhanVien nhanVienDto
    ) {
        try {
            NhanVien existedNhanVien = nhanVienRepository.findById(id).orElse(null);
            log.warn("Id:" + id);
            if (existedNhanVien != null) {
                log.warn("nhanVien found");
                String newMatKhau = (nhanVienDto.getMatKhau()!=null && !nhanVienDto.getMatKhau().isBlank())
                        ? Common.sha256Hash(nhanVienDto.getMatKhau()) : "";
                String newVaiTro = (nhanVienDto.getVaiTro()!=null && !nhanVienDto.getVaiTro().isBlank())
                        ? nhanVienDto.getVaiTro() : "";
                if(!newMatKhau.equals(existedNhanVien.getMatKhau())) {
                    existedNhanVien.setMatKhau(newMatKhau);
                }
                if(!newVaiTro.equals(existedNhanVien.getVaiTro())) {
                    existedNhanVien.setVaiTro(newVaiTro);
                }
                existedNhanVien.setDateUpdated(new Date());
                nhanVienRepository.save(existedNhanVien);
                return ResponseEntity.ok().build();
            }
        } catch (DataIntegrityViolationException e) {
            log.error("Error: " + e);
        } catch (NullPointerException e) {
            log.error("System error: " + e);
        }
        log.warn("nhanVien not found");
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/deactivateStaff")
    @ResponseBody
    public ResponseEntity<String> deactivateStaff(
            @RequestParam(name = "id") int id
    ) {
        try {
            NhanVien existedNhanVien = nhanVienRepository.findById(id).orElse(null);
            if(existedNhanVien!=null) {
                existedNhanVien.setFlagDel(1);
                existedNhanVien.setDateUpdated(new Date());
                nhanVienRepository.save(existedNhanVien);
                return ResponseEntity.ok().build();
            }
        } catch (DataIntegrityViolationException e) {
            log.error("Database error: " + e);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/activateStaff")
    @ResponseBody
    public ResponseEntity<String> activateStaff(
            @RequestParam(name = "id") int id
    ) {
        try {
            NhanVien existedNhanVien = nhanVienRepository.findById(id).orElse(null);
            if(existedNhanVien!=null) {
                existedNhanVien.setFlagDel(0);
                existedNhanVien.setDateUpdated(new Date());
                nhanVienRepository.save(existedNhanVien);
                return ResponseEntity.ok().build();
            }
        } catch (DataIntegrityViolationException e) {
            log.error("Database error: " + e);
        }
        return ResponseEntity.badRequest().build();
    }
}
