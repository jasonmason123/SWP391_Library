package com.springdemo.library.controller;

import com.springdemo.library.model.Sach;
import com.springdemo.library.model.User;
import com.springdemo.library.model.YeuCauMuonSach;
import com.springdemo.library.model.other.SachDuocMuon;
import com.springdemo.library.repositories.SachRepository;
import com.springdemo.library.repositories.UserRepository;
import com.springdemo.library.repositories.YeuCauMuonSachRepository;
import com.springdemo.library.security.userdetails.CustomUserDetails;
import com.springdemo.library.services.GenerateViewService;
import com.springdemo.library.services.JwtService;
import com.springdemo.library.utils.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private SachRepository sachRepository;
    private YeuCauMuonSachRepository yeuCauMuonSachRepository;

    @PostMapping("/process")
    public ResponseEntity<String> getCartFromClient(
            @RequestBody Map<Integer, Integer> clientCart,
            @RequestParam(name = "ngayMuon") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date ngayMuon,
            Authentication authentication
    ) {
        LocalDate localDate = LocalDate.now();
        Date today = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        if(isMaximumNumberOfBooksBorrowed(clientCart) || ngayMuon.before(today)) {
            if(isMaximumNumberOfBooksBorrowed(clientCart)) {
                log.warn("Maximum number of books");
            }
            if(ngayMuon.before(new Date())) {
                log.warn("invalid ngayMuon");
            }
            return ResponseEntity.badRequest().build();
        }
        List<Sach> sachList = sachRepository.findAllById(clientCart.keySet());
        if(!validateQuantities(sachList, clientCart)) {
            return ResponseEntity.badRequest().build();
        }
        User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
        if(user!=null) {
            List<SachDuocMuon> sachDuocMuonList = new ArrayList<>();
            Date ngayTra = new Date(ngayMuon.getTime() + Constants.A_DAY_IN_MILISECONDS*60);
            double totalDeposit = sachList.stream().mapToDouble(sach -> sach.getGiaTien() * clientCart.get(sach.getId())).sum();
            YeuCauMuonSach yeuCauMuonSach = new YeuCauMuonSach(ngayMuon, ngayTra, user, totalDeposit, new Date());
            this.yeuCauMuonSachRepository.save(yeuCauMuonSach);
            for(Sach sach : sachList) {
                SachDuocMuon sachDuocMuon = new SachDuocMuon(sach, yeuCauMuonSach, clientCart.get(sach.getId()));
                sachDuocMuonList.add(sachDuocMuon);
                sach.getSachDuocMuonList().add(sachDuocMuon);
                //Số lượng sách trong kho chỉ bị trừ khi thư viện đã duyệt đơn mượn
                this.sachRepository.save(sach);
            }
            yeuCauMuonSach.setSachDuocMuonList(sachDuocMuonList);
            return ResponseEntity.ok().build();
        }
        log.warn("User not found");
        return ResponseEntity.badRequest().build();
    }

    private boolean validateQuantities(List<Sach> sachList, Map<Integer, Integer> clientCart) {
        for (Sach sach : sachList) {
            int requestedQuantity = clientCart.get(sach.getId());
            if (sach.getSoLuongTrongKho() < requestedQuantity || requestedQuantity <= 0) {
                log.warn("Insufficient quantity for book ID: " + sach.getId());
                return false;
            }
        }
        return true;
    }

    private boolean isMaximumNumberOfBooksBorrowed(Map<Integer, Integer> cart) {
        if(cart.isEmpty()) {
            return false;
        }
        int totalQuantityInCart = 0;
        for(int quantity : cart.values()) {
            if (quantity > 3) {
                return true;
            }
            totalQuantityInCart += quantity;
        }
        return totalQuantityInCart == Constants.MAXIMUM_NUMBER_OF_BOOKS_BORROWED;
    }
}
