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
        return new ModelAndView("home");
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
