package com.springdemo.library.controller.admin;

import com.springdemo.library.model.YeuCauMuonSach;
import com.springdemo.library.repositories.YeuCauMuonSachRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
@Controller
@AllArgsConstructor
@RequestMapping("/management")
public class BookBorrowManagementController {
    YeuCauMuonSachRepository yeuCauMuonSachRepository;

//    SachDuocMuonRepository sachDuocMuonRepository;
    @GetMapping("/manageBookBorrowed")
    public ModelAndView manageBookBorrowed() {
        ModelAndView manageBookBorrowedViewModel = new ModelAndView("admin_and_staff/Layout");
        List<YeuCauMuonSach> list=yeuCauMuonSachRepository.findAll();

//        List<SachDuocMuon> listBorrow=sachDuocMuonRepository.findAll();

        manageBookBorrowedViewModel.addObject("includedPage","admin_and_staff/manageYeuCauMuon");
        manageBookBorrowedViewModel.addObject("title","Quản lí Sách được mượn");
        manageBookBorrowedViewModel.addObject("modelClass",list);
//        manageBookBorrowedViewModel.addObject("modelBook",listBorrow);

        return manageBookBorrowedViewModel;

    }
}
