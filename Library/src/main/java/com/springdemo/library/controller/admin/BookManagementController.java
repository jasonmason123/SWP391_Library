package com.springdemo.library.controller.admin;

import com.springdemo.library.model.*;
import com.springdemo.library.model.Sach;
import com.springdemo.library.model.dto.SachDto;
import com.springdemo.library.repositories.DanhMucRepository;
import com.springdemo.library.repositories.SachRepository;
import com.springdemo.library.repositories.TheLoaiRepository;
import com.springdemo.library.services.GenerateViewService;
import com.springdemo.library.services.SachService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
@Slf4j
@AllArgsConstructor
@RequestMapping("/management/book")

public class BookManagementController {
    private SachRepository sachRepository;
    private DanhMucRepository danhMucRepository;
    private TheLoaiRepository theLoaiRepository;
    private SachService sachService;
    private GenerateViewService generateViewService;

    @GetMapping
    public ModelAndView viewBook(
           @RequestParam(name = "category", required = false) Integer categoryId,
           @RequestParam(name = "genre", required = false) Integer genreId,
           Authentication authentication
    ) {
        ModelAndView manageBookViewModel = generateViewService.generateStaffView("Quản lí Sách", "admin_and_staff/manageBook", authentication);
        Map<String, Object> modelClass = new HashMap<>();
        List<DanhMuc> danhMucList = danhMucRepository.findAll();
        List<TheLoai> theLoaiList;
        List<Sach> sachList;
        if(categoryId!=null) {
            theLoaiList = theLoaiRepository.findTheLoaiByDanhMucId(categoryId);
            sachList = sachRepository.findSachByDanhMucId(categoryId);
            modelClass.put("chosenDanhMuc", categoryId);
        } else {
            sachList = sachRepository.findAll();
            theLoaiList = theLoaiRepository.findAll();
        }
        if(genreId!=null) {
            if(categoryId!=null) {
                sachList = sachList.stream().filter(x -> x.getTheLoaiList().stream().map(TheLoai::getId).toList().contains(genreId)).toList();
            } else {
                sachList = sachRepository.findSachByTheLoaiId(genreId);
            }
            modelClass.put("chosenTheLoai", genreId);
        }
        if(genreId==null && categoryId==null) {
            theLoaiList = theLoaiRepository.findAll();
            sachList = sachRepository.findAll();
        }
        modelClass.put("sachList", sachList);
        modelClass.put("genres", theLoaiList);
        modelClass.put("categories", danhMucList);
        modelClass.put("genresAddUpdate", theLoaiRepository.findAll());
        modelClass.put("categoriesAddUpdate", theLoaiRepository.findAll());
        manageBookViewModel.addObject("modelClass", modelClass);
        return manageBookViewModel;
    }

    @PostMapping("/addBook")
    public ResponseEntity<String> addBook(@RequestBody SachDto sachDTO) {
        Sach sach = Sach.builder()
                .tenSach(sachDTO.getTenSach())
                .tacGia(sachDTO.getTacGia())
                .nhaXuatBan(sachDTO.getNhaXuatBan())
                .moTa(sachDTO.getMoTa())
                .giaTien(sachDTO.getGiaTien())
                .soLuongTrongKho(sachDTO.getSoLuongTrongKho())
                .linkAnh(sachDTO.getLinkAnh())
                .flagDel(sachDTO.getFlagDel())
                .dateCreated(new Date())
                .build();

        sachService.addSach(sach, sachDTO.getTheLoaiId());

        return ResponseEntity.ok("Sach added successfully");
    }

    @PostMapping("/updateBook")
    @ResponseBody
    public ResponseEntity<String> updateBook(
            @RequestParam(name = "id") int id,
            @RequestBody SachDto sachDto
    ) {
        try {
            Sach existedBook = sachService.getSachById(id);

            if (existedBook != null) {
                if (sachDto.getTheLoaiId() != null && !sachDto.getTheLoaiId().isEmpty()) {
                    sachService.updateTheLoaiForSach(existedBook, sachDto.getTheLoaiId());
                }
                String newTacGia = sachDto.getTacGia();
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
                String newNhaXuatBan = (sachDto.getNhaXuatBan()!=null && !sachDto.getNhaXuatBan().isBlank()) ? sachDto.getNhaXuatBan() : "";
                if(!newNhaXuatBan.equals(existedBook.getNhaXuatBan())) {
                    existedBook.setNhaXuatBan(newNhaXuatBan);
                }
                String newMoTa = (sachDto.getMoTa()!=null && !sachDto.getMoTa().isBlank()) ? sachDto.getMoTa() : "";
                if(!newMoTa.equals(existedBook.getMoTa())) {
                    existedBook.setMoTa(newMoTa);
                }
                int newDanhGia = sachDto.getDanhGia();
                if(newDanhGia!=(existedBook.getDanhGia())) {
                    existedBook.setDanhGia(newDanhGia);
                }
                existedBook.setDateUpdated(new Date());
                sachRepository.save(existedBook);
                return ResponseEntity.ok("Book updated successfully");
            } else {
                return ResponseEntity.badRequest().body("Book not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("System error: " + e.getMessage());
        }
    }

    @PostMapping("/hideBook")
    @ResponseBody
    public ResponseEntity<String> hideBook(
            @RequestParam(name = "id") int id
    ) {
        try {
            Sach existedBook = sachRepository.findById(id).orElse(null);
            if(existedBook!=null) {
                existedBook.setFlagDel(1);
                existedBook.setDateUpdated(new Date());
                sachRepository.save(existedBook);
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
            Sach existedBook = sachRepository.findById(id).orElse(null);
            if(existedBook!=null) {
                existedBook.setFlagDel(0);
                existedBook.setDateUpdated(new Date());
                sachRepository.save(existedBook);
                return ResponseEntity.ok().build();
            }
        } catch (DataIntegrityViolationException e) {
            log.error("Database error: " + e);
        }
        return ResponseEntity.badRequest().build();
    }
}
