package com.springdemo.library.controller.admin;


import com.springdemo.library.model.NhanVien;
import com.springdemo.library.repositories.BlogRepository;
import com.springdemo.library.repositories.SachRepository;
import com.springdemo.library.security.userdetails.NhanVienUserDetails;
import com.springdemo.library.services.DashBoardService;
import com.springdemo.library.services.GenerateViewService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@AllArgsConstructor
@RequestMapping("/management/dashboard")
public class DashboardController {

    private final DashBoardService dashboardService;
    private final BlogRepository blogRepository;
    private final GenerateViewService generateViewService;
    private SachRepository sachRepository;

    @GetMapping
    public ModelAndView viewDashboardStaff(Authentication authentication) {
        NhanVien nhanVien = ((NhanVienUserDetails) authentication.getPrincipal()).getNhanVien();
        if(nhanVien.getVaiTro().equals("ROLE_STAFF")) {
            return generateViewService.generateStaffView("Trang chủ", "admin_and_staff/dashboard_staff", authentication);
        } else if (nhanVien.getVaiTro().equals("ROLE_ADMIN")) {
            return generateViewService.generateStaffView("Trang chủ", "admin_and_staff/dashboard_admin", authentication);
        }
        return new ModelAndView("redirect:/error");
    }

/*
________________________________ADMIN STATISTICS________________________________
 */
    @PostMapping("/admin/areaChart")
    @ResponseBody
    public Map<String, Long> getAreaChartData() {
        return dashboardService.getUserAccountByMonth();
    }

    @PostMapping("/admin/staffChart")
    @ResponseBody
    public Map<String, Long> getStaffChartData() {
        return dashboardService.getStaffAccountByMonth();
    }

    @GetMapping("/admin/stats")
    @ResponseBody
    public Map<String, Long> getStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("userCount", dashboardService.countUsers());
        stats.put("sachCount", dashboardService.countSach());
        stats.put("yeuCauMuonSachCount", dashboardService.countYeuCauMuonSach());
        stats.put("nhanVienCount", dashboardService.countNhanVien());
        stats.put("boiThuongCount", dashboardService.countBoiThuong());
        stats.put("datCocCount", dashboardService.countDatCoc());
        stats.put("phiMuonSachCount", dashboardService.countPhiMuonSach());
        return stats;
    }

    //both for admin and staff
    @GetMapping("/pieChart")
    @ResponseBody
    public List<Map<String, Object>> getBooksByCategory() {
        List<Map<String, Object>> result = new ArrayList<>();
        List<Object[]> data = sachRepository.countBooksByCategory();

        for(Object[] row : data) {
            Map<String, Object> map = new HashMap<>();
            map.put("category", row[0]);
            map.put("count", row[1]);
            result.add(map);
        }

        return result;
    }

/*
________________________________STAFF STATISTICS________________________________
 */
    @GetMapping("/staff/stats")
    @ResponseBody
    public Map<String, Long> getStatsStaff() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("boiThuongCount", dashboardService.countBoiThuong());
        stats.put("datCocCount", dashboardService.countDatCoc());
        stats.put("yeuCauMuonSachCount", dashboardService.countPendingYeuCauMuonSach());
        stats.put("phiMuonSachCount", dashboardService.countPhiMuonSach());
        return stats;
    }

    @PostMapping("/staff/areaChart")
    @ResponseBody
    public Map<String, Long> getStaffAreaChartData() {
        return dashboardService.getLoansByMonth();
    }

    @GetMapping("/staff/barChart")
    @ResponseBody
    public List<Map<String, Object>> getStaffBarChartData() {
        List<Map<String, Object>> result = new ArrayList<>();
        List<Object[]> data =blogRepository.countBlogByMonth();
        for(Object[] row : data) {
            Map<String, Object> map = new HashMap<>();
            map.put("category", "Tháng "+row[0]);
            map.put("count", row[1]);
            result.add(map);
        }
        return result;
    }
}
