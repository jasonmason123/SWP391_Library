package com.springdemo.library.controller;

import com.springdemo.library.utils.Common;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Slf4j
@RequestMapping
public class HomeController {

    @GetMapping("/home")
    public ModelAndView home(Authentication authentication) {
        ModelAndView homeViewModel = new ModelAndView("Layout");
        if(Common.isAuthenticated(authentication)
                && authentication.getAuthorities().stream().anyMatch(x -> x.getAuthority().equals("CUSTOMER"))) {
            homeViewModel.addObject("isAuthenticated", 1);
        } else {
            homeViewModel.addObject("isAuthenticated", 0);
        }
        homeViewModel.addObject("breadcrumb", """
                                    <ul>
                                        <li><a href="#">Trang chủ</a></li>
                                        <li><a href="#" class="active">home</a></li>
                                    </ul>""");
        homeViewModel.addObject("title", "home");
        homeViewModel.addObject("includedPage", "home");
        return homeViewModel;
    }

    @GetMapping("/error")
    public ModelAndView error() {
        return new ModelAndView("error-404-page");
    }
}
