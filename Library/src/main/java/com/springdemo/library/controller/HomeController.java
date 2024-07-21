package com.springdemo.library.controller;

import com.springdemo.library.model.Sach;
import com.springdemo.library.model.User;
import com.springdemo.library.model.YeuCauMuonSach;
import com.springdemo.library.model.dto.YeuCauMuonSachViewDto;
import com.springdemo.library.model.other.SachDuocMuon;
import com.springdemo.library.repositories.SachRepository;
import com.springdemo.library.repositories.YeuCauMuonSachRepository;
import com.springdemo.library.security.userdetails.CustomUserDetails;
import com.springdemo.library.services.GenerateViewService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
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
    private YeuCauMuonSachRepository yeuCauMuonSachRepository;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

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
                <li><a href="#" class="active">Trang chủ</a></li>
            </ul>""";
        ModelAndView homeViewModel = generateViewService.generateCustomerView("Trang chủ", breadCrumb, "home", authentication);
        homeViewModel.addObject("modelClass", modelClass);
        return homeViewModel;
    }

    @GetMapping("/aboutus")
    public ModelAndView aboutUs(Authentication authentication) {
        String breadCrumb = """
            <ul>
                <li><a href="/Library/home">Trang chủ</a></li>
                <li><a href="#" class="active">Về chúng tôi</a></li>
            </ul>""";
        return generateViewService.generateCustomerView("Về chúng tôi", breadCrumb, "aboutUs", authentication);
    }

    @GetMapping("/rule")
    public ModelAndView rule(Authentication authentication) {
        String breadCrumb = """
            <ul>
                <li><a href="/Library/home">Trang chủ</a></li>
                <li><a href="#" class="active">Nội quy thư viện</a></li>
            </ul>""";
        return generateViewService.generateCustomerView("Nội quy thư viện", breadCrumb, "rule", authentication);
    }

    @GetMapping("/checkout")
    public ModelAndView checkOut(Authentication authentication) {
        String breadCrumb = """
            <ul>
                <li><a href="/Library/home">Trang chủ</a></li>
                <li><a href="#" class="active">Checkout</a></li>
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

    @GetMapping("/account")
    public ModelAndView account(Authentication authentication) {
        User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
        String breadCrumb = """
            <ul>
                <li><a href="/Library/home">Trang chủ</a></li>
                <li><a href="#" class="active">Tài khoản</a></li>
            </ul>""";
        ModelAndView accountViewModel = generateViewService.generateCustomerView("Tài khoản", breadCrumb,"account", authentication);
        accountViewModel.addObject("modelClass", user);
        return accountViewModel;
    }

    @GetMapping("/borrowing")
    public ModelAndView borrowing(
            Authentication authentication,
            @RequestParam(value = "trangThai", required = false) Integer trangThai,
            @RequestParam(value = "search", required = false) String search
    ) {
        trangThai = trangThai!=null ? trangThai : 2;
        User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
        List<YeuCauMuonSach> yeuCauMuonSachList = yeuCauMuonSachRepository.findAllByNguoiMuonAndTrangThai(user.getId(), trangThai);
        if(search!=null && !search.isEmpty()) {
            yeuCauMuonSachList = yeuCauMuonSachList.stream().filter(x ->
                String.valueOf(x.getId()).contains(search) ||
                (x.getNgayMuon() != null && dateFormat.format(x.getNgayMuon()).contains(search)) ||
                (x.getNgayTra() != null && dateFormat.format(x.getNgayTra()).contains(search))
            ).toList();
        }
        String breadCrumb = """
            <ul>
                <li><a href="/Library/home">Trang chủ</a></li>
                <li><a href="/Library/account">Tài khoản</a></li>
                <li><a href="#" class="active">Sách đang mượn</a></li>
            </ul>""";
        String heading = trangThai==1 ? "Sách đã được đồng ý mượn" : trangThai==0 ? "Sách đang chờ mượn" : "Sách đang mượn";
        Map<String, Object> modelClass = new HashMap<>();
        modelClass.put("yeuCauMuonSachList", yeuCauMuonSachList);
        modelClass.put("trangThai", trangThai);
        modelClass.put("heading", heading);
        ModelAndView borrowingViewModel = generateViewService.generateCustomerView("Sách đang mượn", breadCrumb, "borrowing", authentication);
        borrowingViewModel.addObject("modelClass", modelClass);
        return borrowingViewModel;
    }

    @PostMapping("/borrowing/reportlostbooks")
    public ResponseEntity<String> reportLostBooks(
            @RequestBody Map<String, List<Integer>> lostBookIdRequestBody
    ) {
        try {
            int yeuCauId = lostBookIdRequestBody.get("yeuCau").get(0);
            YeuCauMuonSach yeuCauMuonSach = yeuCauMuonSachRepository.findById(yeuCauId).orElse(null);
            if(yeuCauMuonSach!=null) {
                List<Integer> lostBookIdList =  lostBookIdRequestBody.get("lost");
                for(SachDuocMuon sachDuocMuon : yeuCauMuonSach.getSachDuocMuonList()) {
                    if(lostBookIdList.contains(sachDuocMuon.getSach().getId())) {
                        if(sachDuocMuon.getTrangThai()==0) {
                            sachDuocMuon.setTrangThai(-1);
                        }
                    } else if(!lostBookIdList.contains(sachDuocMuon.getSach().getId()) && sachDuocMuon.getTrangThai()==-1) {
                        sachDuocMuon.setTrangThai(0);
                    }
                }
                yeuCauMuonSachRepository.save(yeuCauMuonSach);
                return ResponseEntity.ok().build();
            }
        } catch (Exception e) {
            log.error("Error: " + e);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/history")
    public ModelAndView history(
            Authentication authentication,
            @RequestParam(value = "search", required = false) String search
    ) {
        User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
        List<YeuCauMuonSach> yeuCauMuonSachList = yeuCauMuonSachRepository.findAllByNguoiMuonAndTrangThaiOrderByNgayTraDesc(user.getId(), 3);
        if(search!=null && !search.isEmpty()) {
            yeuCauMuonSachList = yeuCauMuonSachList.stream().filter(x ->
                    String.valueOf(x.getId()).contains(search) ||
                    (x.getNgayMuon() != null && dateFormat.format(x.getNgayMuon()).contains(search)) ||
                    (x.getNgayTra() != null && dateFormat.format(x.getNgayTra()).contains(search))
            ).toList();
        }
        String breadCrumb = """
            <ul>
                <li><a href="/Library/home">Trang chủ</a></li>
                <li><a href="/Library/account">Tài khoản</a></li>
                <li><a href="#" class="active">Lịch sử mượn sách</a></li>
            </ul>""";
        ModelAndView historyViewModel = generateViewService.generateCustomerView("Lịch sử mượn sách", breadCrumb, "borrow-history", authentication);
        historyViewModel.addObject("modelClass", yeuCauMuonSachList);
        return historyViewModel;
    }

    @GetMapping("/findyeucau")
    @ResponseBody
    public ResponseEntity<YeuCauMuonSachViewDto> findBorrowedBooks(
            @RequestParam("yeucau") int yeuCauId
    ) {
        try {
            YeuCauMuonSach yeuCauMuonSach = yeuCauMuonSachRepository.findById(yeuCauId).get();
            YeuCauMuonSachViewDto yeuCauMuonSachViewDto = new YeuCauMuonSachViewDto(yeuCauMuonSach);
            return ResponseEntity.ok(yeuCauMuonSachViewDto);
        } catch (NullPointerException e) {
            log.error("YeuCauMuonSach with id: " + yeuCauId + " not found!");
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/error")
    public ModelAndView error() {
        return new ModelAndView("error-404-page");
    }
}
