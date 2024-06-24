package com.springdemo.library.controller.admin;

import com.springdemo.library.model.Blog;
import com.springdemo.library.repositories.BlogRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
@Controller
@Slf4j
@AllArgsConstructor
@RequestMapping("/management")
public class BaiVietCanDuyetController {
    BlogRepository blogRepository;


    @GetMapping("/manageBaiVietCanDuyet")
    public ModelAndView manageBookBorrowed() {
        ModelAndView manageBlogViewModel = new ModelAndView("admin_and_staff/Layout");
        List<Blog> list=blogRepository.findAll();



        manageBlogViewModel.addObject("includedPage","admin_and_staff/manageBaiVietcanDuyet");
        manageBlogViewModel.addObject("title","Quản lí Blog");
        manageBlogViewModel.addObject("modelClass",list);


        return manageBlogViewModel;

    }
    @PostMapping("/acceptBlog")
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
    @PostMapping("/rejectBlog")
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

