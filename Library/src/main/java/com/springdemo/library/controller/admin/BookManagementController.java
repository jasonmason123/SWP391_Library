package com.springdemo.library.controller.admin;

import com.springdemo.library.model.Sach;
import com.springdemo.library.repositories.SachRepository;
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

public class BookManagementController {

    private SachRepository SachRepository;
    @GetMapping("/book")
    public ModelAndView viewBook() {
        List<Sach> sachList = SachRepository.findAll();
        ModelAndView manageBookViewModel = new ModelAndView("admin_and_staff/Layout");
        manageBookViewModel.addObject("includedPage","admin_and_staff/manageBook");
        manageBookViewModel.addObject("title","Quản lí Sách");
        manageBookViewModel.addObject("modelClass", sachList);
        return manageBookViewModel;
    }

    @PostMapping("/addBook")
    @ResponseBody
    public ResponseEntity<String> addBook(
            @RequestBody Sach sachDto
    ) {
        try {
            Sach existedBook = SachRepository.findById(sachDto.getId()).orElse(null);
            if (existedBook == null) {
                log.warn("adding");
                SachRepository.save(
                        Sach.builder().tenSach(sachDto.getTenSach())
                                .linkAnh(sachDto.getLinkAnh())
                                .tacGia(sachDto.getTacGia())
                                .giaTien(sachDto.getGiaTien())
                                .soLuongTrongKho(sachDto.getSoLuongTrongKho())
                                .nhaXuatBan(sachDto.getNhaXuatBan())
                                .moTa(sachDto.getMoTa())
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

    @PostMapping("/updateBook")
    @ResponseBody
    public ResponseEntity<String> updateBook(
            @RequestParam(name = "id") int id,
            @RequestBody Sach sachDto
    ) {
        try {
            Sach existedBook = SachRepository.findById(id).orElse(null);
            log.warn("Id:" + id);
            if (existedBook != null) {
                log.warn("Book found");

                String newName = (sachDto.getTenSach()!=null && !sachDto.getTenSach().isBlank())
                        ? sachDto.getTenSach() : "";

                if(!newName.equals(existedBook.getTenSach())) {
                    existedBook.setTenSach(newName);
                }
                String newTacGia = (sachDto.getTacGia()!=null && !sachDto.getTacGia().isBlank())
                        ? sachDto.getTacGia() : "";

                if(!newTacGia.equals(existedBook.getTacGia())) {
                    existedBook.setTacGia(newTacGia);
                }
                double newPrice = sachDto.getGiaTien();

                if(newPrice!=(existedBook.getGiaTien())) {
                    existedBook.setGiaTien(newPrice);
                }
                int newSoLuong = sachDto.getSoLuongTrongKho();

                if(newSoLuong!=(existedBook.getSoLuongTrongKho())) {
                    existedBook.setSoLuongTrongKho(newSoLuong);
                }
                String newNhaXuatBan = (sachDto.getNhaXuatBan()!=null && !sachDto.getNhaXuatBan().isBlank())
                        ? sachDto.getNhaXuatBan() : "";

                if(!newNhaXuatBan.equals(existedBook.getNhaXuatBan())) {
                    existedBook.setNhaXuatBan(newNhaXuatBan);
                }
                String newMoTa = (sachDto.getMoTa()!=null && !sachDto.getMoTa().isBlank())
                        ? sachDto.getMoTa() : "";

                if(!newMoTa.equals(existedBook.getMoTa())) {
                    existedBook.setMoTa(newMoTa);
                }
                int newDanhGia = sachDto.getDanhGia();

                if(newDanhGia!=(existedBook.getDanhGia())) {
                    existedBook.setDanhGia(newDanhGia);
                }
                existedBook.setDateUpdated(new Date());
                SachRepository.save(existedBook);
                return ResponseEntity.ok().build();
            }
        } catch (DataIntegrityViolationException e) {
            log.error("Error: " + e);
        } catch (NullPointerException e) {
            log.error("System error: " + e);
        }
        log.warn("Book not found");
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/hideBook")
    @ResponseBody
    public ResponseEntity<String> hideBook(
            @RequestParam(name = "id") int id
    ) {
        try {
            Sach existedBook = SachRepository.findById(id).orElse(null);
            if(existedBook!=null) {
                existedBook.setFlagDel(1);
                existedBook.setDateUpdated(new Date());
                SachRepository.save(existedBook);
                return ResponseEntity.ok().build();
            }
        } catch (DataIntegrityViolationException e) {
            log.error("Database error: " + e);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/showBook")
    @ResponseBody
    public ResponseEntity<String> showBook(
            @RequestParam(name = "id") int id
    ) {
        try {
            Sach existedBook = SachRepository.findById(id).orElse(null);
            if(existedBook!=null) {
                existedBook.setFlagDel(0);
                existedBook.setDateUpdated(new Date());
                SachRepository.save(existedBook);
                return ResponseEntity.ok().build();
            }
        } catch (DataIntegrityViolationException e) {
            log.error("Database error: " + e);
        }
        return ResponseEntity.badRequest().build();
    }
}
