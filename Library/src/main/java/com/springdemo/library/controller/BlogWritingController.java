package com.springdemo.library.controller;

import com.springdemo.library.model.Blog;
import com.springdemo.library.model.Tag;
import com.springdemo.library.model.User;
import com.springdemo.library.model.dto.BlogDto;
import com.springdemo.library.repositories.BlogRepository;
import com.springdemo.library.repositories.TagRepository;
import com.springdemo.library.security.userdetails.CustomUserDetails;
import com.springdemo.library.services.GenerateViewService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Controller
@Slf4j
@AllArgsConstructor
@RequestMapping
public class BlogWritingController {

    private GenerateViewService generateViewService;
    private BlogRepository blogRepository;
    private TagRepository tagRepository;
    @GetMapping("/drafts")
    public ModelAndView blogDraft(
            @RequestParam(name = "search", required = false) String searchString,
            Authentication authentication
    ) {
        User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
        String breadCrumb = """
                <ul>
                    <li><a href="/Library/home">Trang chủ</a></li>
                    <li><a href="#" class="active">Bản nháp của bạn</a></li>
                </ul>""";
        ModelAndView draftsViewModel = generateViewService.generateCustomerView("Nháp", breadCrumb, "blog-drafts", authentication);
        List<Blog> listOfDrafts = blogRepository.findByFlagDelAndTacGia(-1, user.getId());
        if(searchString!=null && !searchString.isEmpty()) {
            draftsViewModel.addObject("modelClass",
                    listOfDrafts.stream().filter(blog -> blog.getTieuDe().contains(searchString)).toList());
        } else {
            draftsViewModel.addObject("modelClass", listOfDrafts);
        }
        return draftsViewModel;
    }

    @GetMapping("/edit")
    public ModelAndView blogEditing(
            @RequestParam(name = "draft", required = false) Integer blogId,
            Authentication authentication
    ) {
        User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
        StringBuilder breadCrumb = new StringBuilder().append("""
            <ul>
                <li><a href="/Library/home">Trang chủ</a></li>
                <li><a href="/Library/drafts">Bản nháp của bạn</a></li>
                <li><a href="#" class="active">Bản nháp mới</a></li>
            """);
        Map<String, Object> modelClass = new HashMap<>();
        ModelAndView editBlogViewModel = generateViewService.generateCustomerView("Viết blog", breadCrumb.toString(), "blog-editing", authentication);
        if(blogId!=null) {
            Blog blog = blogRepository.findDraftByIdAndTacGiaId(blogId, user.getId()).orElse(null);
            if(blog==null) {
                return new ModelAndView("redirect: /error");
            }
            breadCrumb.append("<li>").append(blog.getTieuDe()).append("</li>");
            modelClass.put("blog", blog);
            modelClass.put("blogTagIds", new ArrayList<>(blog.getTags().stream().map(Tag::getId).toList()));
        }
        breadCrumb.append("</ul>");
        modelClass.put("tacGia", user.getTenUser());
        modelClass.put("tagList", tagRepository.findAll());
        editBlogViewModel.addObject("modelClass", modelClass);
        return editBlogViewModel;
    }

    @PostMapping("/savedraft")
    public ResponseEntity<String> saveDraft(
            @RequestBody BlogDto blogDto,
            Authentication authentication
    ) {
        if((blogDto.getTieuDe()==null || blogDto.getTieuDe().isEmpty()) ||
            blogDto.getNoiDung()==null || blogDto.getNoiDung().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
        Blog blog = blogRepository.findDraftByIdAndTacGiaId(blogDto.getId(), user.getId()).orElse(null);
        if(blog!=null) {
            if(!blog.getTieuDe().equals(blogDto.getTieuDe())) {
                blog.setTieuDe(blogDto.getTieuDe());
            }
            if(!blog.getNoiDung().equals(blogDto.getNoiDung())) {
                blog.setNoiDung(blogDto.getNoiDung());
            }
            List<Integer> tagIds = blogDto.getTagIds();
            if(tagIds!=null && !tagIds.isEmpty()) {
                List<Tag> addedTags = tagRepository.findAllById(tagIds);
                if(!new HashSet<>(blog.getTags()).containsAll(addedTags) || !new HashSet<>(addedTags).containsAll(blog.getTags())) {
                    blog.setTags(addedTags);
                }
            } else {
                if(!blog.getTags().isEmpty()) {
                    blog.getTags().clear();
                }
            }
            blog.setNgayCapNhat(new Date());
            blogRepository.save(blog);
            return ResponseEntity.ok("" + blog.getId());
        } else {
            Blog newBlog = new Blog(user, blogDto.getTieuDe(), blogDto.getNoiDung(), new Date(), -1);
            blogRepository.save(newBlog);
            return ResponseEntity.ok("" + newBlog.getId());
        }
    }

    @PostMapping("/submitblog")
    public ResponseEntity<String> submitBlog(
            @RequestBody Blog blogDto,
            Authentication authentication
    ) {
        if((blogDto.getTieuDe()==null || blogDto.getTieuDe().isEmpty()) ||
                blogDto.getNoiDung()==null || blogDto.getNoiDung().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
        Blog blog = blogRepository.findDraftByIdAndTacGiaId(blogDto.getId(), user.getId()).orElse(null);
        if(blog!=null) {
            blog.setFlagDel(2);
            blogRepository.save(blog);
        } else {
            Blog newBlog = new Blog(user, blogDto.getTieuDe(), blogDto.getNoiDung(), new Date(), 2);
            blogRepository.save(newBlog);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/deletedraft")
    public ResponseEntity<String> deleteDraft(
            @RequestParam(name = "draft") int blogId,
            Authentication authentication
    ) {
        User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
        Blog blog = blogRepository.findDraftByIdAndTacGiaId(blogId, user.getId()).orElse(null);
        if(blog!=null) {
            blogRepository.delete(blog);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
