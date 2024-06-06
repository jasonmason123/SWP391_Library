package com.springdemo.library.controller.admin;

import com.springdemo.library.model.YeuCauMuonSach;
import com.springdemo.library.repositories.YeuCauMuonSachRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@AllArgsConstructor
@RequestMapping("/management")
public class BookBorrowManagementController {
    YeuCauMuonSachRepository yeuCauMuonSachRepository;

    @GetMapping("/manageBookBorrowed")
    public ModelAndView manageBookBorrowed() {
        ModelAndView manageBookBorrowedViewModel = new ModelAndView("Layout");
        List<YeuCauMuonSach> list=yeuCauMuonSachRepository.findAll();
        manageBookBorrowedViewModel.addObject("includedPage","admin/manageBookBorrowed");
        manageBookBorrowedViewModel.addObject("title","Quản lí Sách được mượn");
        manageBookBorrowedViewModel.addObject("modelClass",list);
        return manageBookBorrowedViewModel;
    }
}
