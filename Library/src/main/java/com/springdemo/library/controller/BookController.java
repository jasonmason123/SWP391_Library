package com.springdemo.library.controller;

import com.springdemo.library.model.DanhMuc;
import com.springdemo.library.model.Sach;
import com.springdemo.library.model.TheLoai;
import com.springdemo.library.repositories.DanhMucRepository;
import com.springdemo.library.repositories.SachRepository;
import com.springdemo.library.services.GenerateViewService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
@Slf4j
@AllArgsConstructor
@RequestMapping
public class BookController {
    private GenerateViewService generateViewService;
    private SachRepository sachRepository;
    private DanhMucRepository danhMucRepository;
    private final List<Sach> sachList = new ArrayList<>();
    @GetMapping("/book")
    public ModelAndView book(
            @RequestParam(name = "bookId", required = false) Integer bookId,
            @RequestParam(name = "search", required = false) String searchString,
            @RequestParam(name = "page", required = false) Integer pageNumberParam,
            @RequestParam(name = "theLoai", required = false) Integer theLoaiIdParam,
            @RequestParam(name = "tacGia", required = false) List<String> tacGiaParams,
            @RequestParam(name = "nhaXuatBan", required = false) List<String> nhaXuatBanParams,
            Authentication authentication
    ) {
        try {
            if(bookId==null) {
                String breadCrumb = """
                <ul>
                    <li><a href="#">Trang chủ</a></li>
                    <li><a href="#" class="active">Sách</a></li>
                </ul>""";
                ModelAndView sachViewModel = generateViewService.generateCustomerView("Sách", breadCrumb,"book", authentication);
                Map<String, Object> modelClass = new HashMap<>();
                int pageNumber = pageNumberParam!=null ? pageNumberParam-1 : 0;
                int pageSize = 16;
                int maxPagesToShow = 3;
                if(sachList.isEmpty()) {
                    sachList.addAll(sachRepository.findAll());
                }

                //filter
                List<Sach> filteredSachList;
                if(searchString!=null && !searchString.isEmpty()) {
                    filteredSachList = sachList.stream().filter(sach ->
                            (sach.getTenSach().contains(searchString)) ||
                            (sach.getTacGia().contains(searchString)) ||
                            (sach.getNhaXuatBan().contains(searchString))
                    ).toList();
                    modelClass.put("searchString", searchString);
                } else {
                    if (theLoaiIdParam == null) {
                        filteredSachList = new ArrayList<>(sachList);
                    } else {
                        filteredSachList = sachRepository.findSachByTheLoaiId(theLoaiIdParam);
                        modelClass.put("chosenTheLoaiId", theLoaiIdParam);
                    }
                    if (tacGiaParams != null && !tacGiaParams.isEmpty()) {
                        filteredSachList = filteredSachList.stream()
                                .filter(book -> tacGiaParams.contains(book.getTacGia())).toList();
                        modelClass.put("chosenTacGiaList", tacGiaParams);
                    }
                    if (nhaXuatBanParams != null && !nhaXuatBanParams.isEmpty()) {
                        filteredSachList = filteredSachList.stream()
                                .filter(book -> nhaXuatBanParams.contains(book.getNhaXuatBan())).toList();
                        modelClass.put("chosenNhaXuatBanList", nhaXuatBanParams);
                    }
                }
                
                Pageable pageable = PageRequest.of(pageNumber, pageSize);
                Page<Sach> sachListPaged = generateViewService.generatePagedList(filteredSachList, pageable);
                List<DanhMuc> danhMucList = danhMucRepository.findAll();
                List<String> nhaXuatBanList = theLoaiIdParam==null ?
                        sachRepository.findTopNhaXuatBan() : sachRepository.findTopNhaXuatBanByTheLoaiId(theLoaiIdParam);
                List<String> tacGiaList = theLoaiIdParam==null ?
                        sachRepository.findTopTacGia() : sachRepository.findTopTacGiaByTheLoai(theLoaiIdParam);
                int totalPages = sachListPaged.getTotalPages();
                int totalItems = (int) sachListPaged.getTotalElements();
                int startItem = pageNumber * pageSize + 1;
                int endItem = Math.min(startItem + pageSize - 1, totalItems);

                modelClass.put("page", pageNumberParam);
                modelClass.put("sachList", sachListPaged);
                modelClass.put("danhMucList", danhMucList);
                modelClass.put("nhaXuatBanList", nhaXuatBanList);
                modelClass.put("tacGiaList", tacGiaList);
                modelClass.put("totalPages", totalPages);
                modelClass.put("currentPage", pageNumber);
                modelClass.put("pageNumbers", generateViewService.generatePageNumbers(pageNumber, totalPages, maxPagesToShow));
                modelClass.put("startItem", startItem);
                modelClass.put("endItem", endItem);
                modelClass.put("totalItems", totalItems);
                sachViewModel.addObject("modelClass", modelClass);
                return sachViewModel;
            } else {
                Sach sach = sachRepository.findById(bookId).orElse(null);
                if (sach==null) {
                    return new ModelAndView("redirect:/error");
                }
                String breadCrumb = """
                    <ul>
                        <li><a href="#">Trang chủ</a></li>
                        <li><a href="#">Sách</a></li>
                        <li><a href="#" class="active">""" + sach.getTenSach() +  """
                    </a></li>
                    </ul>""";
                List<Sach> listOfRelatedBooks = sachRepository.findSachByListOfTheLoai(sach.getTheLoaiList().stream().map(TheLoai::getId).toList());
                ModelAndView bookDetailViewModel = generateViewService.generateCustomerView(sach.getTenSach(), breadCrumb, "book-details", authentication);
                Map<String, Object> modelClass = new HashMap<>();
                modelClass.put("sach", sach);
                modelClass.put("listOfRelatedBooks", listOfRelatedBooks);
                bookDetailViewModel.addObject("modelClass", modelClass);
                return bookDetailViewModel;
            }
        } catch (IllegalArgumentException e) {
            log.error("Error: " + e);
            return new ModelAndView("redirect:/error");
        }
    }
}