package com.springdemo.library.controller.admin;

import com.springdemo.library.model.Sach;
import com.springdemo.library.model.dto.MissingSach;
import com.springdemo.library.model.other.SachDuocMuon;
import com.springdemo.library.repositories.SachRepository;
import com.springdemo.library.repositories.YeuCauMuonSachRepository;
import com.springdemo.library.services.GenerateViewService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@Slf4j
@AllArgsConstructor
@RequestMapping("/management/missingbooks")
public class SachDaMatController {
    private YeuCauMuonSachRepository yeuCauMuonSachRepository;
    private SachRepository sachRepository;
    private GenerateViewService generateViewService;

    @GetMapping
    public ModelAndView missingBooks(Authentication authentication) {
        List<MissingSach> missingSachList = yeuCauMuonSachRepository.findAllMissingSach();
        ModelAndView missingSachViewModel = generateViewService.generateStaffView("Sách đã mất", "admin_and_staff/manageSachDaMat", authentication);
        missingSachViewModel.addObject("modelClass", missingSachList);
        return missingSachViewModel;
    }

    @PostMapping("/refill")
    public ResponseEntity<String> refillMissingBook(
            @RequestParam(name = "book") int bookId,
            @RequestParam(name = "quantity") int quantity
    ) {
        Sach sach = sachRepository.findById(bookId).orElse(null);
        if(sach != null) {
            sach.setSoLuongTrongKho(sach.getSoLuongTrongKho() + quantity);
            sachRepository.save(sach);
            List<SachDuocMuon> sachDuocMuonList = yeuCauMuonSachRepository.findAllSachDuocMuonBySachId(bookId);
            sachDuocMuonList.forEach(x -> {
                x.setTrangThai(1);
                yeuCauMuonSachRepository.save(x.getYeuCauMuonSach());
            });
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
