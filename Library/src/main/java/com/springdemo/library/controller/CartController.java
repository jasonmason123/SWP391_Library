package com.springdemo.library.controller;

import com.springdemo.library.model.Sach;
import com.springdemo.library.model.User;
import com.springdemo.library.model.YeuCauMuonSach;
import com.springdemo.library.model.dto.CheckoutDataDto;
import com.springdemo.library.model.other.SachDuocMuon;
import com.springdemo.library.repositories.SachRepository;
import com.springdemo.library.repositories.YeuCauMuonSachRepository;
import com.springdemo.library.security.userdetails.CustomUserDetails;
import com.springdemo.library.utils.Common;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
            @RequestBody CheckoutDataDto checkoutDataDto,
            Authentication authentication
    ) {
        Map<Integer, Double> clientCart = checkoutDataDto.getClientCart();
        Date ngayMuon = checkoutDataDto.getNgayMuon();
        Date ngayTra = checkoutDataDto.getNgayTra();
        String diaChiNhanSach = checkoutDataDto.getDiaChiNhanSach();
        LocalDate localDate = LocalDate.now();
        Date today = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        if(clientCart.size() > 5 || ngayMuon.before(today) || ngayTra.before(today) || ngayTra.before(ngayMuon)) {
            if(clientCart.size() > 5) {
                log.warn("Maximum number of books");
            }
            if(ngayMuon.before(today) || ngayTra.before(today) || ngayTra.before(ngayMuon)) {
                log.warn("invalid ngayMuon and/or ngayTra");
            }
            return ResponseEntity.badRequest().build();
        }
        List<Sach> sachList = sachRepository.findAllById(clientCart.keySet());
        if(!validateQuantities(sachList)) {
            return ResponseEntity.badRequest().build();
        }
        User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
        if(user!=null) {
            List<SachDuocMuon> sachDuocMuonList = new ArrayList<>();
            double totalDeposit = sachList.stream().mapToDouble(Sach::getGiaTien).sum();
            long daysBetween = Common.calculateDaysBetween(ngayMuon, ngayTra);
            double borrowFee = daysBetween * 1000;
            YeuCauMuonSach yeuCauMuonSach = new YeuCauMuonSach(ngayMuon, ngayTra, user, totalDeposit, borrowFee, new Date());
            if(diaChiNhanSach!=null && !diaChiNhanSach.isEmpty()) {
                yeuCauMuonSach.setDiaChiNhanSach(diaChiNhanSach.trim());
            }
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

    private boolean validateQuantities(List<Sach> sachListInCart) {
        for (Sach sach : sachListInCart) {
            if (sach.getSoLuongTrongKho() <= 0) {
                log.warn("Insufficient quantity for book ID: " + sach.getId());
                return false;
            }
        }
        return true;
    }

}
