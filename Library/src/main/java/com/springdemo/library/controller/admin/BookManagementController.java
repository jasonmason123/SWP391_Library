package com.springdemo.library.controller.admin;

import com.springdemo.library.model.*;
import com.springdemo.library.model.Sach;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
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
    public ResponseEntity<String> addBook(
            @RequestParam("tenSach") String tenSach,
            @RequestParam("tacGia") String tacGia,
            @RequestParam("nhaXuatBan") String nhaXuatBan,
            @RequestParam("moTa") String moTa,
            @RequestParam("giaTien") double giaTien,
            @RequestParam("soLuongTrongKho") int soLuongTrongKho,
            @RequestParam("anh") MultipartFile anh,
            @RequestParam("theLoaiId") List<Integer> theLoaiId
    ) {
        String linkAnh = handleFileUpload(anh);
        if(linkAnh.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Sach sach = Sach.builder()
                .tenSach(tenSach)
                .tacGia(tacGia)
                .nhaXuatBan(nhaXuatBan)
                .moTa(moTa)
                .giaTien(giaTien)
                .soLuongTrongKho(soLuongTrongKho)
                .linkAnh(linkAnh)
                .flagDel(0)
                .dateCreated(new Date())
                .build();
        sachService.addSach(sach, theLoaiId);
        return ResponseEntity.ok("Sach added successfully");
    }

    @PostMapping("/updateBook")
    @ResponseBody
    public ResponseEntity<String> updateBook(
            @RequestParam(name = "book") int id,
            @RequestParam("tenSach") String tenSach,
            @RequestParam("tacGia") String tacGia,
            @RequestParam("nhaXuatBan") String nhaXuatBan,
            @RequestParam("moTa") String moTa,
            @RequestParam("giaTien") double giaTien,
            @RequestParam("soLuongTrongKho") int soLuongTrongKho,
            @RequestParam("anh") MultipartFile anh,
            @RequestParam("theLoaiId") List<Integer> theLoaiId
    ) {
        try {
            Sach existedBook = sachService.getSachById(id);

            if (existedBook != null) {
                if(anh!=null) {
                    String linkAnh = handleFileUpload(anh);
                    if(linkAnh.isEmpty()) {
                        return ResponseEntity.badRequest().build();
                    } else {
                        existedBook.setLinkAnh(linkAnh);
                    }
                }
                if(!tenSach.equals(existedBook.getTenSach())) {
                    existedBook.setTenSach(tenSach);
                }
                if (theLoaiId != null && !theLoaiId.isEmpty()) {
                    sachService.updateTheLoaiForSach(existedBook, theLoaiId);
                }
                if(!tacGia.equals(existedBook.getTacGia())) {
                    existedBook.setTacGia(tacGia);
                }
                if(giaTien!=(existedBook.getGiaTien())) {
                    existedBook.setGiaTien(giaTien);
                }
                if(soLuongTrongKho!=(existedBook.getSoLuongTrongKho())) {
                    existedBook.setSoLuongTrongKho(soLuongTrongKho);
                }
                String newNhaXuatBan = (nhaXuatBan!=null && !nhaXuatBan.isBlank()) ? nhaXuatBan : "";
                if(!newNhaXuatBan.equals(existedBook.getNhaXuatBan())) {
                    existedBook.setNhaXuatBan(newNhaXuatBan);
                }
                String newMoTa = (moTa!=null && !moTa.isBlank()) ? moTa : "";
                if(!newMoTa.equals(existedBook.getMoTa())) {
                    existedBook.setMoTa(newMoTa);
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

    @PostMapping("/gettentheloaifromBook")
    @ResponseBody
    public ResponseEntity<List<Integer>> getTenTheLoaiFromBook(@RequestParam(name = "book") int bookId) {
        Sach existedBook = sachRepository.findById(bookId).orElse(null);
        if(existedBook!=null) {
            List<Integer> listOfTenTheLoai = existedBook.getTheLoaiList().stream().map(TheLoai::getId).toList();
            return ResponseEntity.ok(listOfTenTheLoai);
        }
        return ResponseEntity.badRequest().build();
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

    public String handleFileUpload(MultipartFile file) {
        if (file.isEmpty()) {
            log.error("file is empty");
            return "";
        }
        final String uploadPath = "D:/Code/SpringBoot/SWP391_Library/Library/src/main/resources/static/img/product";
        try {
            log.warn("Saving img: " +  file.getOriginalFilename());
            // Save the file locally
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                log.warn("dir not exist");
                uploadDir.mkdirs();
            }

            String fileName = file.getOriginalFilename();
            File destFile = new File(uploadPath + File.separator + fileName);
            log.warn("transfering: " + destFile.getAbsolutePath());
            file.transferTo(destFile);

            return "img/product/" + fileName;
        } catch (IOException e) {
            log.error("Upload image failed!");
            return "";
        }
    }
}
