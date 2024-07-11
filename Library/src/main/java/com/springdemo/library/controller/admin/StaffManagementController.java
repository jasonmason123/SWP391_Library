package com.springdemo.library.controller.admin;

import com.springdemo.library.model.NhanVien;
import com.springdemo.library.model.dto.EmailDetailsDto;
import com.springdemo.library.repositories.NhanVienRepository;
import com.springdemo.library.services.EmailService;
import com.springdemo.library.services.GenerateViewService;
import com.springdemo.library.utils.Common;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
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
@RequestMapping("/management/staff")

public class StaffManagementController {

    private NhanVienRepository nhanVienRepository;
    private GenerateViewService generateViewService;
    private EmailService emailService;
    @GetMapping
    public ModelAndView viewStaff(Authentication authentication) {
        List<NhanVien> nhanVienList = nhanVienRepository.findAll();
        ModelAndView manageStaffViewModel = generateViewService.generateStaffView("Quản lí Nhân viên", "admin_and_staff/manageStaff", authentication);
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
                nhanVienRepository.save(
                        NhanVien.builder().tenNhanVien(nhanVienDto.getTenNhanVien())
                                .matKhau(Common.sha256Hash(nhanVienDto.getMatKhau()))
                                .email(nhanVienDto.getEmail())
                                .soDienThoai(nhanVienDto.getSoDienThoai())
                                .diaChi(nhanVienDto.getDiaChi())
                                .vaiTro(nhanVienDto.getVaiTro().equals("0") ? "ROLE_ADMIN" : nhanVienDto.getVaiTro().equals("1") ? "ROLE_STAFF" : "")
                                .dateCreated(new Date()).build()
                );
                StringBuilder emailBody = new StringBuilder();
                emailBody.append("<html><body>")
                        .append("<p>Gửi anh/chị <strong>").append(nhanVienDto.getTenNhanVien())
                        .append("</strong>,</p><p>Ban quản lý thư viện Therasus đã cấp cho anh/chị tài khoản nhân viên với vai trò <strong>")
                        .append(nhanVienDto.getVaiTro().equals("0") ? "Quản trị viên" : "Thủ thư")
                        .append("</strong><p>Anh/Chị vui lòng đăng nhập vào hệ thống bằng đường link dưới đây:</p>")
                        .append("<a href=\"localhost:8080/Library/management/login\">localhost:8080/Library/management/login</a>")
                        .append("</strong><p>Với email đăng nhập: <strong>").append(nhanVienDto.getEmail())
                        .append("</strong></p><p>Và mật khẩu: <strong>").append(nhanVienDto.getMatKhau())
                        .append("</strong></p><p>Và thực hiện đổi mật khẩu để đảm bảo bảo mật</p>")
                        .append("<p><strong>Ban quản lý thư viện Therasus xin chân thành cảm ơn sự hợp tác của anh/chị</strong></p></body></html>");
                EmailDetailsDto emailDetailsDto = EmailDetailsDto.builder()
                        .recipient(nhanVienDto.getEmail())
                        .subject("[Therasus] Thông báo tạo tài khoản nhân viên cho anh/chị " + nhanVienDto.getTenNhanVien())
                        .messageBody(emailBody.toString())
                        .build();
                emailService.sendHtmlEmail(emailDetailsDto);
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
                newVaiTro = newVaiTro.equals("0") ? "ROLE_ADMIN" : newVaiTro.equals("1") ? "ROLE_STAFF" : "";
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
