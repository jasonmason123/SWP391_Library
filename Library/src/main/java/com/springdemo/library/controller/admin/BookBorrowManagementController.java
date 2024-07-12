package com.springdemo.library.controller.admin;

import com.springdemo.library.model.Sach;
import com.springdemo.library.model.User;
import com.springdemo.library.model.YeuCauMuonSach;
import com.springdemo.library.model.dto.EmailDetailsDto;
import com.springdemo.library.model.dto.SachDuocMuonViewDto;
import com.springdemo.library.model.other.SachDuocMuon;
import com.springdemo.library.repositories.SachRepository;
import com.springdemo.library.repositories.YeuCauMuonSachRepository;
import com.springdemo.library.services.EmailService;
import com.springdemo.library.services.GenerateViewService;
import com.springdemo.library.utils.Common;
import com.springdemo.library.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
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
            @RequestParam("status") int status,
            @RequestParam("p") Double phiVanChuyen
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
                    sach.setSoLuongTrongKho(sach.getSoLuongTrongKho() - 1);
                    sachRepository.save(sach);
                });
                //Nếu yêu cầu được chấp thuận sau ngày mà người mượn đăng ký mượn, ngày mượn sẽ được
                //set lại thành ngày chấp thuận và thời gian mượn sẽ bằng số ngày đã đăng ký từ trước
                if(new Date().after(yeuCauMuonSach.getNgayMuon())) {
                    Date newNgayMuon = new Date();
                    long daysBetween = Common.calculateDaysBetween(yeuCauMuonSach.getNgayMuon(), yeuCauMuonSach.getNgayTra());
                    Date newNgayTra = Common.addDays(newNgayMuon, daysBetween);
                    yeuCauMuonSach.setNgayMuon(newNgayMuon);
                    yeuCauMuonSach.setNgayTra(newNgayTra);
                    yeuCauMuonSachRepository.save(yeuCauMuonSach);
                }
                if(yeuCauMuonSach.getDiaChiNhanSach()!=null && !yeuCauMuonSach.getDiaChiNhanSach().isEmpty() && phiVanChuyen!=null) {
                    yeuCauMuonSach.setPhiVanChuyen(phiVanChuyen);
                    yeuCauMuonSachRepository.save(yeuCauMuonSach);
                }
                sendConfirmationEmail(status, yeuCauMuonSach);
            } else if(status==-1) {
                if(yeuCauMuonSach.getTrangThai()==1) {
                    yeuCauMuonSach.getSachDuocMuonList().forEach(x -> {
                        Sach sach = x.getSach();
                        sach.setSoLuongTrongKho(sach.getSoLuongTrongKho() + 1);
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
            long daysBetween = Common.calculateDaysBetween(yeuCauMuonSach.getNgayMuon(), yeuCauMuonSach.getNgayTra());
            String diaChiNhanSach = yeuCauMuonSach.getDiaChiNhanSach();
            String subject = "Thông báo xác nhận mượn sách";
            StringBuilder messageBodyBuilder = new StringBuilder();
            messageBodyBuilder.append("""
                    <html><body><h2>Thư viện cộng đồng Therasus đã chấp thuận yêu cầu mượn sách của bạn</h2>
                        <h4>Chi tiết</h4><p>Số ID yêu cầu: <strong>""").append(yeuCauMuonSach.getId())
                    .append("</strong></p><p>Email người mượn: <strong>").append(yeuCauMuonSach.getNguoiMuon().getEmail())
                    .append("</strong></p><p>Ngày mượn: <strong>").append(yeuCauMuonSach.getNgayMuon())
                    .append("</strong></p><p>Ngày trả: <strong>").append(yeuCauMuonSach.getNgayTra())
                    .append("</strong></p><div><p>Danh sách sách đăng ký mượn:</p><ul>");
            for(SachDuocMuon sachDuocMuon : yeuCauMuonSach.getSachDuocMuonList()) {
                messageBodyBuilder.append("<li><strong>").append(sachDuocMuon.getSach().getTenSach()).append("</strong>: <span>Đặt cọc: ")
                    .append(sachDuocMuon.getSoTienDatCoc()).append(" đ</span></li>");
            }
            messageBodyBuilder.append("</ul></div><p>Số tiền cần đặt cọc: <strong>").append(yeuCauMuonSach.getSoTienDatCoc()).append(" đ")
                    .append("</strong></p><p>Phí mượn: ").append(daysBetween).append(" ngày x 1000đ/ngày = <strong>")
                    .append(yeuCauMuonSach.getPhiMuonSach()).append(" đ</strong></p>").append("<p><strong>Tổng cộng: ")
                    .append(yeuCauMuonSach.getSoTienDatCoc() + yeuCauMuonSach.getPhiMuonSach()).append("</strong></p>");
            if(diaChiNhanSach!=null && !diaChiNhanSach.isEmpty()) {
                messageBodyBuilder.append("<p>Sách sẽ được giao tới địa chỉ: <strong>").append(diaChiNhanSach).append("</strong></p>").append("""
                <div>Bạn đọc vui lòng đóng phí cọc sách và phí vận chuyển (theo báo giá của đơn vị vận chuyển) qua tài khoản
                    Ngân hàng Thương mại cổ phần Đầu tư và Phát triển Việt Nam (BIDV)
                    <ul>
                        <li>Tên tài khoản: BUI MINH SON</li>
                        <li>Số tài khoản: 1280829588</li>
                        <li>Chi nhánh ngân hàng: BIDV chi nhánh Chương Dương</li>
                        <li>Nội dung chuyển khoản: <strong>Họ tên người đóng phí - """).append(yeuCauMuonSach.getId()).append(" - ")
                    .append(yeuCauMuonSach.getSoTienDatCoc() + yeuCauMuonSach.getPhiMuonSach()).append(" đ</strong></li></ul></div>");
            } else {
                messageBodyBuilder.append("<p>Vui lòng thanh toán tiền đặt cọc tại lễ tân thư viện khi đến nhận sách.</p>");
            }
            messageBodyBuilder.append("<h4>*Vui lòng không xóa email này. Email này sẽ được dùng để xác nhận khi bạn trả sách.</h4></body></html>");
            emailService.sendHtmlEmail(EmailDetailsDto.builder()
                    .recipient(receipientEmail).subject(subject).messageBody(messageBodyBuilder.toString()).build());
        } else if(status==-1) {
            String subject = "Thông báo xác nhận mượn sách";
            String messageBody = """
                    <html>
                    <body>
                        <h2>Thư viện cộng đồng Therasus đã từ chối yêu cầu mượn sách của bạn.</h2>
                        <div>Chúng tôi rất xin lỗi vì sự bất tiện này!</div>
                    </body>
                    </html>
                    """;
            emailService.sendHtmlEmail(EmailDetailsDto.builder()
                    .recipient(receipientEmail).subject(subject).messageBody(messageBody).build());
        }
    }

    @Scheduled(cron = "0 0 0 * * ?") // Run daily at midnight
    public void notifyUsersOfDueDateIn3Days() {
        List<YeuCauMuonSach> allRequests = yeuCauMuonSachRepository.findYeuCauWhereDueDateIsIn3Days();
        for (YeuCauMuonSach request : allRequests) {
            sendDueDateNotification(request, false);
        }
    }

    @Scheduled(cron = "0 0 0 * * ?") // Run daily at midnight
    public void notifyUsersOfDueDate() {
        List<YeuCauMuonSach> allRequests = yeuCauMuonSachRepository.findYeuCauWhereDueDateIsToday();
        for (YeuCauMuonSach request : allRequests) {
            sendDueDateNotification(request, true);
        }
    }

    @Scheduled(cron = "0 0 0 * * ?") // Run daily at midnight
    public void applyPenaltyOnOverdue() {
        List<YeuCauMuonSach> allRequests = yeuCauMuonSachRepository.findAllOverdueYeuCau();
        for (YeuCauMuonSach request : allRequests) {
            double soTienDatCoc = request.getSoTienDatCoc();
            double boiThuong = request.getBoiThuong();
            if(soTienDatCoc > 0) {
                request.setQuaHan(request.getQuaHan() + 1);
                if(soTienDatCoc - 1000 < 0) {
                    request.setSoTienDatCoc(0);
                    request.setBoiThuong(request.getBoiThuong() + (1000 - soTienDatCoc));
                } else {
                    request.setSoTienDatCoc(soTienDatCoc - 1000);
                    request.setBoiThuong(boiThuong + 1000);
                }
                request.setDateUpdated(new Date());
                yeuCauMuonSachRepository.save(request);
            }
        }
    }

    private void sendDueDateNotification(YeuCauMuonSach yeuCauMuonSach, boolean isDueDateToday) {
        User nguoiMuon = yeuCauMuonSach.getNguoiMuon();
        String recipient = nguoiMuon.getEmail();
        String subject = "[Therasus] Thông báo sắp đến hạn trả sách";
        StringBuilder messageBody = new StringBuilder();
        messageBody.append("<html><body><h3>Thông báo tới ").append(nguoiMuon.getTenUser()).append("</h3>");
        if(isDueDateToday) {
            messageBody.append("<p>Yêu cầu mượn sách của bạn sẽ hết hạn trong <strong>HÔM NAY</strong>.</p>")
                    .append("<p>Nếu những sách bạn đã mượn không được hoàn trả đầy đủ trong hôm nay, mức phạt trả muộn sẽ được áp dụng (<strong>1000đ/ngày</strong>)</p>");
        } else {
            messageBody.append("<p>Yêu cầu mượn sách của bạn sẽ hết hạn trong vòng <strong>3</strong> ngày.</p>");
        }
        messageBody.append("<h3>Yêu cầu mượn sách của ").append(nguoiMuon.getTenUser()).append("</h3>")
                .append("<p>Số yêu cầu: <strong>").append(yeuCauMuonSach.getId()).append("</strong></p>")
                .append("<p>Danh sách những sách đang mượn:</p>").append("<ul>");
        for (SachDuocMuon sachDuocMuon : yeuCauMuonSach.getSachDuocMuonList()) {
            messageBody.append("<li>").append(sachDuocMuon.getSach().getTenSach()).append(" <strong>x").append("</strong></li>");
        }
        messageBody.append("</ul><h5>Bạn đọc vui lòng trả đầy đủ toàn bộ sách đã mượn đúng hạn</h5>")
                .append("<h5>Nếu trả không đúng hạn, thư viện sẽ phạt với mức 1000đ/ngày. Tiền phạt sẽ bị trừ vào tiền đặt cọc</h5>")
                .append("</body></html>");
        emailService.sendHtmlEmail(EmailDetailsDto.builder().recipient(recipient).subject(subject).messageBody(messageBody.toString()).build());
    }

}
