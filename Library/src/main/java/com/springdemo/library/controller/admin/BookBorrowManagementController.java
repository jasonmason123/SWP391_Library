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
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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
        List<YeuCauMuonSach> yeuCauMuonSachList=yeuCauMuonSachRepository.findAllYeuCauOrderByDateCreated();
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
            @RequestParam(value = "p", required = false) Double phiVanChuyen
    ) {
        //0:Chua duoc duyet, 1:Da duyet - cho muon, 2:Dang muon, 3:Da tra
        try {
            Date today = new Date();
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
                if(yeuCauMuonSach.getSachDuocMuonList().stream().anyMatch(x -> x.getSach().getSoLuongTrongKho()<=0)) {
                    log.warn("Insuffcient amount");
                    return ResponseEntity.badRequest().body("Insuffcient amount");
                }
                yeuCauMuonSach.getSachDuocMuonList().forEach(x -> {
                    Sach sach = x.getSach();
                    sach.setSoLuongTrongKho(sach.getSoLuongTrongKho() - 1);
                    sachRepository.save(sach);
                });
                //Nếu yêu cầu được chấp thuận sau ngày mà người mượn đăng ký mượn, ngày mượn sẽ được
                //set lại thành ngày chấp thuận và thời gian mượn sẽ bằng số ngày đã đăng ký từ trước
                if(new Date().after(yeuCauMuonSach.getNgayMuon())) {
                    long daysBetween = Common.calculateDaysBetween(yeuCauMuonSach.getNgayMuon(), yeuCauMuonSach.getNgayTra());
                    Date newNgayTra = Common.addDays(today, daysBetween);
                    yeuCauMuonSach.setNgayMuon(today);
                    yeuCauMuonSach.setNgayTra(newNgayTra);
                    yeuCauMuonSachRepository.save(yeuCauMuonSach);
                }
                if(yeuCauMuonSach.getDiaChiNhanSach()!=null && !yeuCauMuonSach.getDiaChiNhanSach().isEmpty() && phiVanChuyen!=null) {
                    yeuCauMuonSach.setPhiVanChuyen(phiVanChuyen);
                    yeuCauMuonSachRepository.save(yeuCauMuonSach);
                }
                sendBorrowConfirmationEmail(status, yeuCauMuonSach);
            } else if(status==-1) {
                if(yeuCauMuonSach.getTrangThai()==1) {
                    yeuCauMuonSach.getSachDuocMuonList().forEach(x -> {
                        Sach sach = x.getSach();
                        sach.setSoLuongTrongKho(sach.getSoLuongTrongKho() + 1);
                        sachRepository.save(sach);
                    });
                }
                sendBorrowConfirmationEmail(status, yeuCauMuonSach);
                yeuCauMuonSachRepository.delete(yeuCauMuonSach);
                return ResponseEntity.ok().build();
            } else if(status==3) {
                yeuCauMuonSach.getSachDuocMuonList().forEach(x -> {
                    Sach sach = x.getSach();
                    sach.setSoLuongTrongKho(sach.getSoLuongTrongKho() + 1);
                    sachRepository.save(sach);
                });
                sendReturnConfimationEmail(yeuCauMuonSach);
            }
            yeuCauMuonSach.setTrangThai(status);
            yeuCauMuonSach.setDateUpdated(new Date());
            yeuCauMuonSachRepository.save(yeuCauMuonSach);
            return ResponseEntity.ok().build();
        } catch (NullPointerException e) {
            log.error("Error: " + e);
            return ResponseEntity.badRequest().build();
        }
    }

    private void sendBorrowConfirmationEmail(int status, YeuCauMuonSach yeuCauMuonSach) {
        String receipientEmail = yeuCauMuonSach.getNguoiMuon().getEmail();
        if(status==1) {
            long daysBetween = Common.calculateDaysBetween(yeuCauMuonSach.getNgayMuon(), yeuCauMuonSach.getNgayTra());
            String diaChiNhanSach = yeuCauMuonSach.getDiaChiNhanSach();
            String subject = "[THERASUS] Thông báo xác nhận mượn sách";
            StringBuilder messageBodyBuilder = new StringBuilder();
            double tongCong = yeuCauMuonSach.getSoTienDatCoc() + yeuCauMuonSach.getPhiMuonSach();
            StringBuilder qrUrl = new StringBuilder();

            messageBodyBuilder.append("""
                    <html><body><h2>Thư viện cộng đồng Therasus đã chấp thuận yêu cầu mượn sách của bạn</h2>
                        <h4>Chi tiết</h4><p>Số ID yêu cầu: <strong>""").append(yeuCauMuonSach.getId())
                    .append("</strong></p><p>Email người mượn: <strong>").append(yeuCauMuonSach.getNguoiMuon().getEmail())
                    .append("</strong></p><p>Ngày mượn: <strong>").append(yeuCauMuonSach.getNgayMuon())
                    .append("</strong></p><p>Ngày trả: <strong>").append(yeuCauMuonSach.getNgayTra())
                    .append("</strong></p><div><p>Danh sách sách đăng ký mượn:</p><ul>");
            for(SachDuocMuon sachDuocMuon : yeuCauMuonSach.getSachDuocMuonList()) {
                messageBodyBuilder.append("<li><strong>").append(sachDuocMuon.getSach().getTenSach()).append("</strong>- <span>Đặt cọc: ")
                    .append(sachDuocMuon.getSoTienDatCoc()).append(" đ</span></li>");
            }
            messageBodyBuilder.append("</ul></div><p>Số tiền cần đặt cọc: <strong>").append(yeuCauMuonSach.getSoTienDatCoc()).append(" đ")
                    .append("</strong></p><p>Phí mượn: ").append(daysBetween).append(" ngày x 1000đ/ngày = <strong>")
                    .append(yeuCauMuonSach.getPhiMuonSach()).append(" đ</strong></p>");
            if(diaChiNhanSach!=null && !diaChiNhanSach.isEmpty()) {
                tongCong += yeuCauMuonSach.getPhiVanChuyen();
                messageBodyBuilder.append("<p>Phí vận chuyển (theo báo giá của đơn vị vận chuyển): <strong>")
                        .append(yeuCauMuonSach.getPhiVanChuyen()).append(" đ</strong></p>");
            }
            messageBodyBuilder.append("<p><strong>Tổng cộng: ").append(tongCong).append(" đ</strong></p>");
            if(diaChiNhanSach!=null && !diaChiNhanSach.isEmpty()) {
                messageBodyBuilder.append("<p>Sách sẽ được giao tới địa chỉ: <strong>").append(diaChiNhanSach).append("</strong></p>");
            }
            //qrurl
            qrUrl.append("https://img.vietqr.io/image/bidv-1280829588-compact2.png?amount=").append(tongCong)
                    .append("&addInfo=THU_VIEN_CONG_DONG_THERASUS-TIEN_COC_SACH_VA_PHI_MUON_SACH");
            if(diaChiNhanSach!=null && !diaChiNhanSach.isEmpty()) {
                qrUrl.append("_VA_PHI_VAN_CHUYEN_CUA_");
            } else {
                qrUrl.append("_CUA_");
            }
            qrUrl.append(yeuCauMuonSach.getNguoiMuon().getTenUser()).append("&accountName=BUI%20MINH%20SON");
            messageBodyBuilder.append("<p>Bạn đọc vui lòng đóng đầy đủ phí và tiền cọc qua tài khoản Ngân hàng Thương mại cổ phần Đầu tư và Phát triển Việt Nam (BIDV) dưới đây</p>")
                    .append("<img src='").append(qrUrl).append("'/>").append("""
                            <ul>
                                <li>Số tài khoản: <strong>1280829588</strong></li>
                                <li>Tên tài khoản: <strong>BUI MINH SON</strong></li>
                                <li>Nội dung chuyển khoản: <strong>(Tên người chuyển) - mượn sách từ THERASUS - """)
                    .append(tongCong).append("</strong></li></ul>").append("<p>Hoặc đóng tiền trực tiếp tại thư viện khi đến nhận sách</p>")
                    .append("<h4>*Vui lòng không xóa email này. Email này sẽ được dùng để xác nhận khi bạn trả sách.</h4></body></html>");
            emailService.sendHtmlEmail(EmailDetailsDto.builder()
                    .recipient(receipientEmail).subject(subject).messageBody(messageBodyBuilder.toString()).build());
        } else if(status==-1) {
            String subject = "Thông báo xác nhận mượn sách";
            String messageBody = """
                    <html><body>
                        <h2>Thư viện cộng đồng Therasus đã từ chối yêu cầu mượn sách của bạn.</h2>
                        <div>Chúng tôi rất xin lỗi vì sự bất tiện này!</div>
                    </body></html>""";
            emailService.sendHtmlEmail(EmailDetailsDto.builder()
                    .recipient(receipientEmail).subject(subject).messageBody(messageBody).build());
        }
    }

    private void sendReturnConfimationEmail(YeuCauMuonSach yeuCauMuonSach) {
        double returnAmount = (yeuCauMuonSach.getSoTienDatCoc() - yeuCauMuonSach.getBoiThuong() > 0) ?
                yeuCauMuonSach.getSoTienDatCoc() - yeuCauMuonSach.getBoiThuong() : 0;
        String receipientEmail = yeuCauMuonSach.getNguoiMuon().getEmail();
        String subject = "[THERASUS] Thông báo xác nhận trả sách";
        StringBuilder messageBodyBuilder = new StringBuilder();

        messageBodyBuilder.append("<html><body><h2>Thư viện cộng đồng Therasus đã tiếp nhận trả sách từ bạn</h2>")
                .append("<p>Số ID yêu cầu: <strong>").append(yeuCauMuonSach.getId()).append("</strong></p>")
                .append("<p>Người mượn: <strong>").append(yeuCauMuonSach.getNguoiMuon().getTenUser()).append("</strong></p>")
                .append("<p>Ngày mượn: <strong>").append(yeuCauMuonSach.getNgayMuon()).append("</strong></p>")
                .append("<p>Ngày trả: <strong>").append(yeuCauMuonSach.getNgayTra()).append("</strong></p>")
                .append("<p>Danh sách sách đăng ký mượn:</p><div><ul>");
        for(SachDuocMuon sachDuocMuon : yeuCauMuonSach.getSachDuocMuonList()) {
            messageBodyBuilder.append("<li><strong>").append(sachDuocMuon.getSach().getTenSach()).append("</strong>- <span>Đặt cọc: ")
                    .append(sachDuocMuon.getSoTienDatCoc()).append(" đ</span></li>");
        }
        messageBodyBuilder.append("</ul></div><p>Số tiền đã đặt cọc: <strong>").append(yeuCauMuonSach.getSoTienDatCoc()).append(" đ")
                .append("<p>Số ngày quá hạn: <strong>").append(yeuCauMuonSach.getQuaHan()).append("</strong> ngày")
                .append("<p>Phí bồi thường: <strong>").append(yeuCauMuonSach.getBoiThuong()).append(" </strong> đ")
                .append("<p><strong>Số tiền trả lại: ").append(returnAmount).append(" </strong> đ</p>")
                .append("Bạn đọc vui lòng đến nhận lại tiền cọc tại thư viện hoặc gửi thông tin số tài khoản tới email: sonbmhe180353@fpt.edu.vn");
        emailService.sendHtmlEmail(EmailDetailsDto.builder()
                .recipient(receipientEmail).subject(subject).messageBody(messageBodyBuilder.toString()).build());
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
                    request.setBoiThuong(request.getBoiThuong() + (1000 - soTienDatCoc));
                } else {
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
