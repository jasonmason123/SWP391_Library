package com.springdemo.library.controller.admin;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@AllArgsConstructor
@RequestMapping("/management")
public class BaiVietMoiController {

    @GetMapping("/manageBaiVietMoi")
    public ModelAndView manageBaiVietMoi() {
        ModelAndView manageBaiVietMoiViewModel = new ModelAndView("admin_and_staff/Layout");

        manageBaiVietMoiViewModel.addObject("includedPage","admin_and_staff/manageBaiVietMoi");
        manageBaiVietMoiViewModel.addObject("title","Quản lí Bài Viết Mới");

        return manageBaiVietMoiViewModel;

    }
}
