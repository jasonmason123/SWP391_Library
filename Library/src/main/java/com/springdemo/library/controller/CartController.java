package com.springdemo.library.controller;

import com.springdemo.library.model.Sach;
import com.springdemo.library.model.User;
import com.springdemo.library.model.YeuCauMuonSach;
import com.springdemo.library.model.other.SachDuocMuon;
import com.springdemo.library.repositories.SachRepository;
import com.springdemo.library.security.userdetails.CustomUserDetails;
import com.springdemo.library.services.JwtService;
import com.springdemo.library.utils.Constants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/process")
    public ResponseEntity<String> getCartFromClient(
            @RequestBody Map<Integer, Integer> clientCart,
            @RequestParam(name = "ngayMuon")Date ngayMuon,
            @RequestParam(name = "ngayTra")Date ngayTra,
            Authentication authentication
    ) {
        if(isMaximumNumberOfBooksBorrowed(clientCart)) {
            return ResponseEntity.badRequest().build();
        }
        List<Sach> sachList = sachRepository.findAllById(clientCart.keySet());
        if(!validateQuantities(sachList, clientCart)) {
            return ResponseEntity.badRequest().build();
        }
        User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
        if(user!=null) {
            List<SachDuocMuon> sachDuocMuonList = new ArrayList<>();
            YeuCauMuonSach yeuCauMuonSach = new YeuCauMuonSach(ngayMuon, ngayTra, user, new Date());
            double totalDeposit = 0;
            for(Sach sach : sachList) {
                SachDuocMuon sachDuocMuon = new SachDuocMuon(sach, yeuCauMuonSach, clientCart.get(sach.getId()));
                sachDuocMuonList.add(sachDuocMuon);
                sach.getSachDuocMuonList().add(sachDuocMuon);
                totalDeposit += (sach.getGiaTien() * clientCart.get(sach.getId()));
            }
            yeuCauMuonSach.getSachDuocMuonList().addAll(sachDuocMuonList);
            yeuCauMuonSach.setSoTienDatCoc(totalDeposit);
            return ResponseEntity.ok().build();
        }
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
            totalQuantityInCart += quantity;
        }
        return totalQuantityInCart == Constants.MAXIMUM_NUMBER_OF_BOOKS_BORROWED;
    }
}
