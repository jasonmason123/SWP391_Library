package com.springdemo.library.controller.admin;

import com.springdemo.library.model.Blog;
import com.springdemo.library.model.NhanVien;
import com.springdemo.library.repositories.BlogRepository;
import com.springdemo.library.services.GenerateViewService;
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
@AllArgsConstructor
@Slf4j
@RequestMapping("/management/manageDanhSachBaiViet")
public class DanhSachBaiVietController {
    private BlogRepository blogRepository;
    private GenerateViewService generateViewService;

    @GetMapping
    public ModelAndView manageBookBorrowed(Authentication authentication) {
        ModelAndView manageBlogViewModel = generateViewService.generateStaffView("Quản lí Blog", "admin_and_staff/manageDanhSachBaiViet", authentication);
        List<Blog> list = blogRepository.findByFlagDelIn(List.of(0, 1));
        manageBlogViewModel.addObject("modelClass",list);
        return manageBlogViewModel;

    }
    @PostMapping("/showBlog")
    @ResponseBody
    public ResponseEntity<String> showBlog(
            @RequestParam(name = "id") int id
    ) {
        try {
            Blog existedBlog = blogRepository.findById(id).orElse(null);
            if(existedBlog!=null) {
                existedBlog.setFlagDel(0);
                blogRepository.save(existedBlog);
                return ResponseEntity.ok().build();
            }
        } catch (DataIntegrityViolationException e) {
            log.error("Database error: " + e);
        }
        return ResponseEntity.badRequest().build();
    }
    @PostMapping("/hideBlog")
    @ResponseBody
    public ResponseEntity<String> hideBlog(
            @RequestParam(name = "id") int id
    ) {
        try {
            Blog existedBlog = blogRepository.findById(id).orElse(null);
            if(existedBlog!=null) {
                existedBlog.setFlagDel(1);

                blogRepository.save(existedBlog);
                return ResponseEntity.ok().build();
            }
        } catch (DataIntegrityViolationException e) {
            log.error("Database error: " + e);
        }
        return ResponseEntity.badRequest().build();
    }
}

