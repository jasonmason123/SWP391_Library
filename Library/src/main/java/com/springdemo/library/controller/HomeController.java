package com.springdemo.library.controller;

import com.springdemo.library.model.Sach;
import com.springdemo.library.repositories.SachRepository;
import com.springdemo.library.services.GenerateViewService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@AllArgsConstructor
@RequestMapping
public class HomeController {

    private GenerateViewService generateViewService;
    private SachRepository sachRepository;

    @GetMapping("/home")
    public ModelAndView home(Authentication authentication) {
        List<Sach> top10NewsestBooks = sachRepository.findTop10NewestBooks();
        List<Sach> mostBorrowedBooks = sachRepository.findTopMostBorrowedBooks();
        List<Sach> top4MostBorrowedBooks = mostBorrowedBooks.subList(0, Math.min(mostBorrowedBooks.size(), 4));
        Map<String, Object> modelClass = new HashMap<>();
        modelClass.put("top10NewsestBooks", top10NewsestBooks);
        modelClass.put("top4MostBorrowedBooks", top4MostBorrowedBooks);
        String breadCrumb = """
            <ul>
                <li><a href="#">Trang chủ</a></li>
            </ul>""";
        ModelAndView homeViewModel = generateViewService.generateCustomerView("Trang chủ", breadCrumb, "home", authentication);
        homeViewModel.addObject("modelClass", modelClass);
        return homeViewModel;
    }

    @GetMapping("/aboutus")
    public ModelAndView aboutUs(Authentication authentication) {
        String breadCrumb = """
            <ul>
                <li><a href="#">Trang chủ</a></li>
                <li><a href="#">Về chúng tôi</a></li>
            </ul>""";
        return generateViewService.generateCustomerView("Về chúng tôi", breadCrumb, "aboutUs", authentication);
    }

    @GetMapping("/rule")
    public ModelAndView rule(Authentication authentication) {
        String breadCrumb = """
            <ul>
                <li><a href="#">Trang chủ</a></li>
                <li><a href="#">Nội quy thư viện</a></li>
            </ul>""";
        return generateViewService.generateCustomerView("Nội quy thư viện", breadCrumb, "rule", authentication);
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

    @GetMapping("/gallery")
    public ModelAndView gallery(Authentication authentication) {
        String breadCrumb = """
            <ul>
                <li><a href="/Library/home">Trang chủ</a></li>
                <li><a href="#" class="active">Thư viện ảnh </a></li>
            </ul>""";
        return generateViewService.generateCustomerView("Thư viện ảnh", breadCrumb, "libimg", authentication);
    }

    @GetMapping("/slogan")
    public ModelAndView slogan(Authentication authentication) {
        String breadCrumb = """
            <ul>
                <li><a href="/Library/home">Trang chủ</a></li>
                <li><a href="#" class="active">Phòng trưng bày trích dẫn</a></li>
            </ul>""";
        return generateViewService.generateCustomerView("Phòng trưng bày trích dẫn", breadCrumb, "slogan", authentication);
    }

    @GetMapping("/error")
    public ModelAndView error() {
        return new ModelAndView("error-404-page");
    }
}
