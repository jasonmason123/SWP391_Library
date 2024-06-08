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
public class HomeController {
    @GetMapping("/home")
    public ModelAndView home() {
        ModelAndView homeViewModel = new ModelAndView("Layout");
        homeViewModel.addObject("breadcrumb", "<ul>\n" +
                "                        <li><a href=\"#\">Trang chủ</a></li>\n" +
                "                        <li><a href=\"#\" class=\"active\">home</a></li>\n" +
                "                    </ul>");
        homeViewModel.addObject("title", "home");
        homeViewModel.addObject("includedPage", "home");
        return homeViewModel;
    }

    @GetMapping("/anotherhome")
    public ModelAndView anotherHome() {
        return new ModelAndView("anotherhome");
    }

    @GetMapping("/error")
    public ModelAndView error() {
        return new ModelAndView("error-404-page");
    }
}
