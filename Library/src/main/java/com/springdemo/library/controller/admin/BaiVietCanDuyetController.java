package com.springdemo.library.controller.admin;

import com.springdemo.library.model.Blog;
import com.springdemo.library.model.dto.EmailDetailsDto;
import com.springdemo.library.repositories.BlogRepository;
import com.springdemo.library.services.EmailService;
import com.springdemo.library.services.GenerateViewService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;
@Controller
@Slf4j
@AllArgsConstructor
@RequestMapping("/management")
public class BaiVietCanDuyetController {
    private BlogRepository blogRepository;
    private GenerateViewService generateViewService;
    private EmailService emailService;

    @GetMapping("/manageBaiVietCanDuyet")
    public ModelAndView manageBookBorrowed(Authentication authentication) {
        ModelAndView manageBlogViewModel = generateViewService.generateStaffView("Quản lí Blog", "admin_and_staff/manageBaiVietcanDuyet", authentication);
        List<Blog> list = blogRepository.findByFlagDel(2);
        manageBlogViewModel.addObject("modelClass",list);
        return manageBlogViewModel;
    }

    @GetMapping("/previewblog")
    public ModelAndView previewBlog(
            @RequestParam("blog") int blogId,
            Authentication authentication
    ) {
        Blog idleBlog = blogRepository.findByIdAndFlagDel(blogId, 2).orElse(null);
        if(idleBlog!=null) {
            ModelAndView previewBlogViewModel = generateViewService.generateStaffView("Xem trước Blog", "admin_and_staff/previewBlog", authentication);
            previewBlogViewModel.addObject("modelClass", idleBlog);
            return previewBlogViewModel;
        } else {
            return new ModelAndView("redirect: /error");
        }
    }

    @PostMapping("/acceptBlog")
    @ResponseBody
    public ResponseEntity<String> acceptBlog(
            @RequestParam(name = "id") int id
    ) {
        try {
            Blog existedBlog = blogRepository.findByIdAndFlagDel(id, 2).orElse(null);
            if(existedBlog!=null) {
                existedBlog.setFlagDel(0);
                existedBlog.setNgayTao(new Date());
                blogRepository.save(existedBlog);
                String emailBody = """
                        <html><body>
                        <h3>Ban quản lý thư viện cộng đồng Therasus xin thông báo:</h3>
                        <p>Bài viết <strong>""" + existedBlog.getTieuDe() + """
                        </strong> đã được <strong>CHẤP THUẬN</strong> và đã được phát hành</p>
                        <p>Bạn có thể xem bài viết của mình tại:</p>
                        <a href="localhost:8080/Library/blog?blog=""" + existedBlog.getId() + """
                        ">localhost:8080/Library/blog?blog=""" + existedBlog.getId() + """
                        </a><p>Thư viện cộng đồng Therasus xin chân thành cảm ơn bạn đã đóng góp bài viết</p>
                        </body></html>""";
                emailService.sendHtmlEmail(new EmailDetailsDto(
                        existedBlog.getTacGia().getEmail(),
                        emailBody,
                        "[Therasus] Thông báo về việc bài viết của bạn đã được chấp thuận",
                        ""
                ));
                return ResponseEntity.ok().build();
            }
        } catch (DataIntegrityViolationException e) {
            log.error("Database error: " + e);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/rejectBlog")
    @ResponseBody
    public ResponseEntity<String> rejectBlog(
            @RequestParam(name = "id") int id
    ) {
        try {
            Blog existedBlog = blogRepository.findByIdAndFlagDel(id, 2).orElse(null);
            if(existedBlog!=null) {
                existedBlog.setFlagDel(-1);
                blogRepository.save(existedBlog);
                String emailBody = """
                        <html><body>
                        <h3>Ban quản lý thư viện cộng đồng Therasus xin thông báo:</h3>
                        <p>Bài viết <strong>""" + existedBlog.getTieuDe() + """
                        </strong> đã bị <strong>TỪ CHỐI</strong> do đã vi phạm quy định của thư viện về nội dung blog</p>
                        <p>Bạn hãy chỉnh sửa lại nội dung bài viết để phù hợp với quy định của thư viện</p>
                        <p>Để được hỗ trợ, vui lòng liên hệ tới hotline: <strong>1900 1234</strong></p>
                        </body></html>""";
                emailService.sendHtmlEmail(new EmailDetailsDto(
                        existedBlog.getTacGia().getEmail(),
                        emailBody,
                        "[Therasus] Thông báo về việc bài viết của bạn đã bị từ chối",
                        ""
                ));
                return ResponseEntity.ok().build();
            }
        } catch (DataIntegrityViolationException e) {
            log.error("Database error: " + e);
        }
        return ResponseEntity.badRequest().build();
    }
}

