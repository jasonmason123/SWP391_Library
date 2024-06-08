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
public class BookController {
    @GetMapping("/book")
    public ModelAndView Book() {
        ModelAndView bookViewModel = new ModelAndView("Layout");
        bookViewModel.addObject("breadcrumb", """
                                    <ul>
                                        <li><a href="#">Trang chủ</a></li>
                                        <li><a href="#" class="active">Book</a></li>
                                    </ul>""");
        bookViewModel.addObject("title", "book");
        bookViewModel.addObject("includedPage", "book");
        return bookViewModel;
    }
}