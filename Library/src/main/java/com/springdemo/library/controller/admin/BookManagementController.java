package com.springdemo.library.controller.admin;

import com.springdemo.library.model.*;
import com.springdemo.library.model.Sach;
import com.springdemo.library.model.dto.SachDto;
import com.springdemo.library.repositories.DanhMucRepository;
import com.springdemo.library.repositories.SachRepository;
import com.springdemo.library.repositories.TheLoaiRepository;
import com.springdemo.library.services.SachService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.util.Optional;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
@AllArgsConstructor
@RequestMapping("/management")

public class BookManagementController {



    private SachRepository SachRepository;
    private DanhMucRepository DanhMucRepository;
    private TheLoaiRepository TheLoaiRepository;
    private SachService sachService;

    @GetMapping("/book")
    public ModelAndView viewBook() {
        List<Sach> sachList = SachRepository.findAll();
        List<TheLoai> theLoaiList=TheLoaiRepository.findAll();
        ModelAndView manageBookViewModel = new ModelAndView("admin_and_staff/Layout");
        manageBookViewModel.addObject("includedPage","admin_and_staff/manageBook");
        manageBookViewModel.addObject("title","Quản lí Sách");
        manageBookViewModel.addObject("modelClass", sachList);
        List<DanhMuc> danhMucList = DanhMucRepository.findAll();
        manageBookViewModel.addObject("categories",danhMucList);
        manageBookViewModel.addObject("genres", theLoaiList);
        return manageBookViewModel;
    }

    @GetMapping("/searchBook")
    public ModelAndView searchBookByCategory(@RequestParam String category,String genre, Model model) {
        List<Sach> books;
        if ("all".equals(category) && "all".equals(genre)) {
            books = SachRepository.findAll();
        } else if ("all".equals(category)) {
            books = SachRepository.findSachByTheLoai(genre);
        } else if ("all".equals(genre)) {
            books = SachRepository.findSachByDanhMuc(category);
        } else {
            books = SachRepository.findSachByDanhMucVaTheLoai(category,genre);
        }

        model.addAttribute("genres", TheLoaiRepository.findAll());

        model.addAttribute("modelClass", books);
        ModelAndView manageBookViewModel = new ModelAndView("admin_and_staff/Layout");
        manageBookViewModel.addObject("includedPage","admin_and_staff/manageBook");
        manageBookViewModel.addObject("title","Quản lí Sách");

        List<DanhMuc> danhMucList = DanhMucRepository.findAll();
        manageBookViewModel.addObject("categories",danhMucList);

        return manageBookViewModel;

    }

    @PostMapping("/addBook")
    public ResponseEntity<String> addBook(@RequestBody SachDto sachDTO) {
        Sach sach = Sach.builder()
                .tenSach(sachDTO.getTenSach())
                .tacGia(sachDTO.getTacGia())
                .nhaXuatBan(sachDTO.getNhaXuatBan())
                .moTa(sachDTO.getMoTa())
                .danhGia(sachDTO.getDanhGia())
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
                String newName = (sachDto.getTenSach() != null && !sachDto.getTenSach().isBlank()) ? sachDto.getTenSach() : existedBook.getTenSach();
                existedBook.setTenSach(newName);

                String newTacGia = (sachDto.getTacGia() != null && !sachDto.getTacGia().isBlank()) ? sachDto.getTacGia() : existedBook.getTacGia();
                existedBook.setTacGia(newTacGia);

                String newPrice = (sachDto.getGiaTien() != null && !sachDto.getGiaTien().isBlank()) ? sachDto.getGiaTien() : existedBook.getGiaTien();
                existedBook.setGiaTien(newPrice);

                String newSoLuong = (sachDto.getSoLuongTrongKho() != null && !sachDto.getSoLuongTrongKho().isBlank()) ? sachDto.getSoLuongTrongKho() : existedBook.getSoLuongTrongKho();
                existedBook.setSoLuongTrongKho(newSoLuong);

                String newNhaXuatBan = (sachDto.getNhaXuatBan() != null && !sachDto.getNhaXuatBan().isBlank()) ? sachDto.getNhaXuatBan() : existedBook.getNhaXuatBan();
                existedBook.setNhaXuatBan(newNhaXuatBan);

                String newMoTa = (sachDto.getMoTa() != null && !sachDto.getMoTa().isBlank()) ? sachDto.getMoTa() : existedBook.getMoTa();
                existedBook.setMoTa(newMoTa);

                String newDanhGia = (sachDto.getDanhGia() != null && !sachDto.getDanhGia().isBlank()) ? sachDto.getDanhGia() : existedBook.getDanhGia();
                existedBook.setDanhGia(newDanhGia);

                if (sachDto.getTheLoaiId() != null && !sachDto.getTheLoaiId().isEmpty()) {
                    sachService.updateTheLoaiForSach(existedBook, sachDto.getTheLoaiId());
                }

                existedBook.setDateUpdated(new Date());
                SachRepository.save(existedBook);
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
