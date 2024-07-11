package com.springdemo.library.controller.admin;

import com.springdemo.library.model.Sach;
import com.springdemo.library.model.YeuCauMuonSach;
import com.springdemo.library.model.dto.EmailDetailsDto;
import com.springdemo.library.model.dto.SachDuocMuonViewDto;
import com.springdemo.library.model.other.SachDuocMuon;
import com.springdemo.library.repositories.SachRepository;
import com.springdemo.library.repositories.YeuCauMuonSachRepository;
import com.springdemo.library.services.EmailService;
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

import java.util.ArrayList;
import java.util.List;
@Controller
@Slf4j
@AllArgsConstructor
@RequestMapping("/management/manageBookBorrowed")
public class BookBorrowManagementController {

    private YeuCauMuonSachRepository yeuCauMuonSachRepository;
    private SachRepository sachRepository;
    private EmailService emailService;
    private GenerateViewService generateViewService;

    @GetMapping
    public ModelAndView manageBookBorrowed(Authentication authentication) {
        ModelAndView manageBookBorrowedViewModel = generateViewService.generateStaffView("Quản lí Sách được mượn", "admin_and_staff/manageYeuCauMuon", authentication);
        List<YeuCauMuonSach> yeuCauMuonSachList=yeuCauMuonSachRepository.findAll();
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
            if(status==1) {
                yeuCauMuonSach.getSachDuocMuonList().forEach(x -> {
                    Sach sach = x.getSach();
                    sach.setSoLuongTrongKho(sach.getSoLuongTrongKho() - x.getSoLuongMuon());
                    sachRepository.save(sach);
                });
                sendConfirmationEmail(status, yeuCauMuonSach);
            } else if(status==-1) {
                if(yeuCauMuonSach.getTrangThai()==1) {
                    yeuCauMuonSach.getSachDuocMuonList().forEach(x -> {
                        Sach sach = x.getSach();
                        sach.setSoLuongTrongKho(sach.getSoLuongTrongKho() + x.getSoLuongMuon());
                        sachRepository.save(sach);
                    });
                }
                sendConfirmationEmail(status, yeuCauMuonSach);
                yeuCauMuonSachRepository.delete(yeuCauMuonSach);
                return ResponseEntity.ok().build();
            }
            yeuCauMuonSach.setTrangThai(status);
            yeuCauMuonSachRepository.save(yeuCauMuonSach);
            return ResponseEntity.ok().build();
        } catch (NullPointerException e) {
            log.error("Error: " + e);
            return ResponseEntity.badRequest().build();
        }
    }

    private void sendConfirmationEmail(int status, YeuCauMuonSach yeuCauMuonSach) {
        String receipientEmail = yeuCauMuonSach.getNguoiMuon().getEmail();
        if(status==1) {
            String subject = "Thông báo xác nhận mượn sách";
            StringBuilder messageBodyBuilder = new StringBuilder();
            messageBodyBuilder.append("""
                    <html><body>
                    <h2>Thư viện cộng đồng Therasus đã chấp thuận yêu cầu mượn sách của bạn</h2>
                        <h4>Chi tiết</h4><p>Số ID yêu cầu: <strong>""")
                    .append(yeuCauMuonSach.getId())
                    .append("</strong></p><p>Email người mượn: <strong>")
                    .append(yeuCauMuonSach.getNguoiMuon().getEmail())
                    .append("</strong></p><p>Ngày mượn: <strong>")
                    .append(yeuCauMuonSach.getNgayMuon())
                    .append("</strong></p><div><p>Danh sách sách đăng ký mượn:</p><ul>");
            for(SachDuocMuon sachDuocMuon : yeuCauMuonSach.getSachDuocMuonList()) {
                messageBodyBuilder.append("<li><strong>")
                    .append(sachDuocMuon.getSach().getTenSach())
                    .append("</strong> | <span>Số lượng: <strong>")
                    .append(sachDuocMuon.getSoLuongMuon())
                    .append("</strong></span></li>");
            }
            messageBodyBuilder.append("</ul></div>");
            messageBodyBuilder.append("<p>Số tiền cần đặt cọc: <strong>");
            messageBodyBuilder.append(yeuCauMuonSach.getSoTienDatCoc());
            messageBodyBuilder.append("</strong></p><p>Vui lòng thanh toán tiền đặt cọc tại lễ tân thư viện khi đến nhận sách</p></body></html>");
            emailService.sendHtmlEmail(EmailDetailsDto.builder()
                    .recipient(receipientEmail).subject(subject).messageBody(messageBodyBuilder.toString()).build());
        } else if(status==-1) {
            String subject = "Thông báo xác nhận mượn sách";
            String messageBody = """
                    <html>
                    <body>
                        <h2>Thư viện cộng đồng Therasus đã từ chối yêu cầu mượn sách của bạn</h2>
                        <div>Chúng tôi rất xin lỗi vì sự bất tiện này!</div>
                    </body>
                    </html>
                    """;
            emailService.sendHtmlEmail(EmailDetailsDto.builder()
                    .recipient(receipientEmail).subject(subject).messageBody(messageBody).build());
        }
    }
}
