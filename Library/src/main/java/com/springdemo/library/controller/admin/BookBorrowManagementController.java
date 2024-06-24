package com.springdemo.library.controller.admin;

import com.springdemo.library.model.YeuCauMuonSach;
import com.springdemo.library.model.dto.SachDuocMuonViewDto;
import com.springdemo.library.repositories.YeuCauMuonSachRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
@Controller
@Slf4j
@AllArgsConstructor
@RequestMapping("/management")
public class BookBorrowManagementController {

    private YeuCauMuonSachRepository yeuCauMuonSachRepository;

    @GetMapping("/manageBookBorrowed")
    public ModelAndView manageBookBorrowed() {
        ModelAndView manageBookBorrowedViewModel = new ModelAndView("admin_and_staff/Layout");
        List<YeuCauMuonSach> yeuCauMuonSachList=yeuCauMuonSachRepository.findAll();
        manageBookBorrowedViewModel.addObject("includedPage","admin_and_staff/manageYeuCauMuon");
        manageBookBorrowedViewModel.addObject("title","Quản lí Sách được mượn");
        manageBookBorrowedViewModel.addObject("modelClass",yeuCauMuonSachList);
        return manageBookBorrowedViewModel;
    }

    @GetMapping("/findBorrowedBooks")
    public ResponseEntity<List<SachDuocMuonViewDto>> findBorrowedBooks(
            @RequestParam("yeuCauId") int yeuCauId
    ) {
        try {
            YeuCauMuonSach yeuCauMuonSach = yeuCauMuonSachRepository.findById(yeuCauId).get();
            List<SachDuocMuonViewDto> sachDuocMuonList = new ArrayList<>();
            yeuCauMuonSach.getSachDuocMuonList().forEach(sachDuocMuon -> sachDuocMuonList.add(new SachDuocMuonViewDto(sachDuocMuon)));
            return ResponseEntity.ok(sachDuocMuonList);
        } catch (NullPointerException e) {
            log.error("YeuCauMuonSach with id: " + yeuCauId + " not found!");
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/updateRequestStatus")
    public ResponseEntity<String> updateRequestStatus(
            @RequestParam("yeuCauId") int yeuCauId,
            @RequestParam("status") int status
    ) {
        //-1:Tu choi, 0:Chua duoc duyet, 1:Da duyet - cho muon, 2:Dang muon, 3:Da tra
        try {
            YeuCauMuonSach yeuCauMuonSach = yeuCauMuonSachRepository.findById(yeuCauId).get();
            //Ngoài từ chối, chỉ được phép cập nhật lên trạng thái tiếp theo (VD: 1->2, 2->3, không đc 1->3)
            if((status!=-1 && (status <= yeuCauMuonSach.getTrangThai() || status > yeuCauMuonSach.getTrangThai()+1))
                    || (status==-1 && yeuCauMuonSach.getTrangThai()>=2) //Từ sau khi sách đang đc mượn, không đc từ chối
                    || (yeuCauMuonSach.getTrangThai()==-1) //Khi đã từ chối, không thể cập nhật lên các trạng thái khác
                    || (status<-1 || status>3) //Giá trị biên
            ) {
                log.warn("Invalid status");
                return ResponseEntity.badRequest().body("Invalid status");
            }
            yeuCauMuonSach.setTrangThai(status);
            yeuCauMuonSachRepository.save(yeuCauMuonSach);
            return ResponseEntity.ok().build();
        } catch (NullPointerException e) {
            log.error("YeuCauMuonSach with id: " + yeuCauId + " not found!");
            return ResponseEntity.badRequest().build();
        }
    }
}
