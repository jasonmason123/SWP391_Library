package com.springdemo.library.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Slf4j
@AllArgsConstructor
@RequestMapping
public class BlogController {
    @GetMapping("/blog")
    public ModelAndView Blog() {
        ModelAndView blogViewModel = new ModelAndView("Layout");
        blogViewModel.addObject("breadcrumb", "<ul>\n" +
                "                        <li><a href=\"#\">Trang chủ</a></li>\n" +
                "                        <li><a href=\"#\" class=\"active\">Blog</a></li>\n" +
                "                    </ul>");
        blogViewModel.addObject("title", "blog");
        blogViewModel.addObject("includedPage", "blog");
        return blogViewModel;
    }

    @GetMapping("/blogDetail")
    public ModelAndView blogDetail() {
        ModelAndView blogDetailsViewModel = new ModelAndView("Layout");
        blogDetailsViewModel.addObject("breadcrumb", "<ul>\n" +
                "                        <li><a href=\"#\">Blog</a></li>\n" +
                "                        <li><a href=\"#\" class=\"active\">Chi tiết blog</a></li>\n" +
                "                    </ul>");
        blogDetailsViewModel.addObject("title", "blog-details");
        blogDetailsViewModel.addObject("includedPage", "blog-details");
        return blogDetailsViewModel;

    }
}

