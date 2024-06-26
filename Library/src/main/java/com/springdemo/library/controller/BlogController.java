package com.springdemo.library.controller;

import com.springdemo.library.model.Blog;
import com.springdemo.library.model.Tag;
import com.springdemo.library.repositories.BlogRepository;
import com.springdemo.library.repositories.TagRepository;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@AllArgsConstructor
@RequestMapping
public class BlogController {
    private BlogRepository blogRepository;
    private TagRepository tagRepository;
    private GenerateViewService generateViewService;
    @GetMapping("/blog")
    public ModelAndView blog(
            @RequestParam(name = "id", required = false) Integer blogId,
            @RequestParam(name = "search", required = false) String searchString,
            @RequestParam(name = "tag", required = false) Integer tagId,
            @RequestParam(name = "page", required = false) Integer pageNumberParam,
            Authentication authentication
    ) {
        if(blogId==null) {
            int pageNumber = pageNumberParam!=null ? pageNumberParam-1 : 0;
            int pageSize = 5;
            int maxPagesToShow = 5;
            Pageable pageable = PageRequest.of(pageNumber, pageSize);

            Page<Blog> blogList = blogRepository.findAll(pageable);
            List<Tag> tagList = tagRepository.findAll();
            int totalItems = 30*16; //(int) sachList.getTotalElements();
            int startItem = pageNumber * pageSize + 1;
            int endItem = Math.min(startItem + pageSize - 1, totalItems);

            String breadCrumb =  """
            <ul>
                <li><a href="#">Trang chủ</a></li>
                <li><a href="#" class="active">Blog</a></li>
            </ul>""";
            ModelAndView blogViewModel = generateViewService.generateCustomerView("Blog", breadCrumb, "blog", authentication);

            Map<String, Object> modelClass = new HashMap<>();
            modelClass.put("blogList", blogList);
            modelClass.put("tagList", tagList);
            modelClass.put("totalPages", 30);
            modelClass.put("currentPage", pageNumber);
            modelClass.put("pageNumbers", generateViewService.generatePageNumbers(pageNumber, 30, maxPagesToShow));
            modelClass.put("startItem", startItem);
            modelClass.put("endItem", endItem);
            modelClass.put("totalItems", totalItems);
            blogViewModel.addObject("modelClass", modelClass);
            return blogViewModel;
        } else {
            Blog blog = blogRepository.findById(blogId).orElse(null);
            if(blog==null) {
                return new ModelAndView("redirect:/error");
            }
            String breadCrumb =  """
            <ul>
                <li><a href="#">Trang chủ</a></li>
                <li><a href="#" class="active">Blog</a></li>
                <li><a href="#" class="active">""" + blog.getTieuDe() + """
            </a></li>
            </ul>""";
            ModelAndView blogDetailViewModel = generateViewService.generateCustomerView(blog.getTieuDe(), breadCrumb, "blog-details", authentication);
            blogDetailViewModel.addObject("modelClass", blog);
            return blogDetailViewModel;
        }
    }
}
