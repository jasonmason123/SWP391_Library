package com.springdemo.library.controller;

import com.springdemo.library.services.GenerateViewService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Slf4j
@AllArgsConstructor
@RequestMapping
public class HomeController {

    private GenerateViewService generateViewService;

    @GetMapping("/home")
    public ModelAndView home(Authentication authentication) {
        String breadCrumb = """
            <ul>
                <li><a href="#">Trang chủ</a></li>
            </ul>""";
        return generateViewService.generateCustomerView("Trang chủ", breadCrumb, "home", authentication);
    }

    @GetMapping("/aboutus")
    public ModelAndView aboutUs(Authentication authentication) {
        String breadCrumb = """
            <ul>
                <li><a href="#">Trang chủ</a></li>
                <li><a href="#">Về chúng tôi</a></li>
            </ul>""";
        return generateViewService.generateCustomerView("Về chúng tôi", breadCrumb, "AboutUs", authentication);
    }

    @GetMapping("/rule")
    public ModelAndView rule(Authentication authentication) {
        String breadCrumb = """
            <ul>
                <li><a href="#">Trang chủ</a></li>
                <li><a href="#">Nội quy thư viện</a></li>
            </ul>""";
        return generateViewService.generateCustomerView("Nội quy thư viện", breadCrumb, "Rule", authentication);
    }

    @GetMapping("/checkout")
    public ModelAndView checkOut(Authentication authentication) {
        String breadCrumb = """
            <ul>
                <li><a href="#">Trang chủ</a></li>
                <li><a href="#"></a>Checkout</li>
            </ul>""";
        ModelAndView checkOutViewModel = generateViewService.generateCustomerView("Checkout", breadCrumb, "checkout", authentication);
        checkOutViewModel.addObject("noCart", 0);
        return checkOutViewModel;
    }

    @GetMapping("/error")
    public ModelAndView error() {
        return new ModelAndView("error-404-page");
    }
}
