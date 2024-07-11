package com.springdemo.library.services;

import com.springdemo.library.model.NhanVien;
import com.springdemo.library.security.userdetails.CustomUserDetails;
import com.springdemo.library.security.userdetails.NhanVienUserDetails;
import com.springdemo.library.utils.Common;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class GenerateViewService {

    public ModelAndView generateView(String viewName, Authentication authentication) {
        ModelAndView viewModel = new ModelAndView(viewName);
        if(Common.isAuthenticated(authentication)
                && authentication.getAuthorities().stream().anyMatch(x -> x.getAuthority().equals("CUSTOMER"))) {
            viewModel.addObject("isAuthenticated", 1);
        } else {
            viewModel.addObject("isAuthenticated", 0);
        }
        return viewModel;
    }

    public ModelAndView generateStaffView(String title, String includedPage, Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof NhanVienUserDetails)) {
            return new ModelAndView("redirect:/error");
        } else {
            ModelAndView viewModel = new ModelAndView("admin_and_staff/Layout");
            NhanVien nhanVien = ((NhanVienUserDetails) authentication.getPrincipal()).getNhanVien();
            viewModel.addObject("role", nhanVien.getVaiTro());
            viewModel.addObject("nhanVienEmail", nhanVien.getEmail());
            viewModel.addObject("title", title);
            viewModel.addObject("includedPage", includedPage);
            return viewModel;
        }
    }


    public ModelAndView generateCustomerView(String title, String breadCrumb, String includedPage, Authentication authentication) {
        ModelAndView viewModel = new ModelAndView("Layout");
        if(Common.isAuthenticated(authentication)
                && authentication.getAuthorities().stream().anyMatch(x -> x.getAuthority().equals("CUSTOMER"))) {
            viewModel.addObject("isAuthenticated", 1);
            String userName = ((CustomUserDetails) authentication.getPrincipal()).getUsername();
            viewModel.addObject("userName", userName);
        } else {
            viewModel.addObject("isAuthenticated", 0);
        }
        viewModel.addObject("title", title);
        viewModel.addObject("breadcrumb", breadCrumb);
        viewModel.addObject("includedPage", includedPage);
        return viewModel;
    }

    public List<Integer> generatePageNumbers(int currentPage, int totalPages, int maxPagesToShow) {
        List<Integer> pageNumbers = new ArrayList<>();

        int halfPagesToShow = maxPagesToShow / 2;

        // Calculate start and end page numbers
        int startPage = Math.max(0, currentPage - halfPagesToShow);
        int endPage = Math.min(totalPages - 1, currentPage + halfPagesToShow);

        // Adjust if we're showing fewer pages at the start or end
        if (currentPage - startPage < halfPagesToShow) {
            endPage = Math.min(totalPages - 1, endPage + (halfPagesToShow - (currentPage - startPage)));
        }
        if (endPage - currentPage < halfPagesToShow) {
            startPage = Math.max(0, startPage - (halfPagesToShow - (endPage - currentPage)));
        }

        // Ensure first and last pages are included
        if (startPage > 0) {
            pageNumbers.add(0);
            if (startPage > 1) {
                pageNumbers.add(-1); // Placeholder for '...'
            }
        }

        for (int i = startPage; i <= endPage; i++) {
            pageNumbers.add(i);
        }

        if (endPage < totalPages - 1) {
            if (endPage < totalPages - 2) {
                pageNumbers.add(-1); // Placeholder for '...'
            }
            pageNumbers.add(totalPages - 1);
        }

        return pageNumbers;
    }

    public <T> Page<T> generatePagedList(List<T> list, Pageable pageRequest) {
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), list.size());

        List<T> pageContent = list.subList(start, end);
        return new PageImpl<>(pageContent, pageRequest, list.size());
    }
}
