package com.springdemo.library.controller.admin;

import com.springdemo.library.model.Tag;
import com.springdemo.library.repositories.TagRepository;
import com.springdemo.library.services.GenerateViewService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

@Controller
@Slf4j
@AllArgsConstructor
@RequestMapping("/management/blogtag")
public class BlogTagController {
    private TagRepository tagRepository;
    private GenerateViewService generateViewService;

    @GetMapping
    public ModelAndView blogTag(Authentication authentication) {
        List<Tag> tagList = tagRepository.findAll();
        ModelAndView tagViewModel = generateViewService.generateStaffView("Tag", "admin_and_staff/manageBlogTag", authentication);
        tagViewModel.addObject("modelClass", tagList);
        return tagViewModel;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addTag(
            @RequestParam(name = "tag") String tenTag
    ) {
        try {
            tagRepository.save(new Tag(tenTag, new Date()));
            return ResponseEntity.ok().build();
        } catch (DataIntegrityViolationException e) {
            log.error("Error: " + e);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/delete")
    public ResponseEntity<String> updateTag(
            @RequestParam(name = "tag") int tagId
    ) {
        Tag tag = tagRepository.findById(tagId).orElse(null);
        if(tag!=null) {
            tagRepository.delete(tag);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
