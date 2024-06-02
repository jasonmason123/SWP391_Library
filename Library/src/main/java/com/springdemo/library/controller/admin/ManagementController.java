package com.springdemo.library.controller.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Slf4j
@AllArgsConstructor
@RequestMapping("/management")

public class ManagementController {
    @GetMapping("/manageStaff")
    public ModelAndView manageStaff() {
        ModelAndView manageStaffViewModel = new ModelAndView("admin_and_staff/Layout");
        manageStaffViewModel.addObject("includedPage","admin_and_staff/manageStaff");
        manageStaffViewModel.addObject("title","Quản lí Nhân viên");
        return manageStaffViewModel;
    } //first Spring Boot Code of TMinh


}