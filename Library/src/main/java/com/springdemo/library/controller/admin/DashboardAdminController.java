package com.springdemo.library.controller.admin;


import com.springdemo.library.repositories.SachRepository;
import com.springdemo.library.services.DashBoardService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
@RequestMapping("/management")
public class DashboardAdminController {

    private final DashBoardService dashboardService;
    private SachRepository sachRepository;

    @GetMapping("/dashboard_admin")
    public ModelAndView viewDashboardAdmin() {
        ModelAndView manageBookViewModel = new ModelAndView("admin_and_staff/Layout");
        manageBookViewModel.addObject("includedPage", "admin_and_staff/dashboard_admin");
        manageBookViewModel.addObject("title", "Dashboard Admin");
        return manageBookViewModel;
    }
    @PostMapping("/areaChart")
    @ResponseBody
    public Map<String, Long> getAreaChartData() {
        return dashboardService.getUserAccountByMonth();
    }

    @PostMapping("/staffChart")
    @ResponseBody
    public Map<String, Long> getStaffChartData() {
        return dashboardService.getStaffAccountByMonth();
    }

    @GetMapping("/stats")
    @ResponseBody
    public Map<String, Long> getStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("userCount", dashboardService.countUsers());
        stats.put("sachCount", dashboardService.countSach());
        stats.put("yeuCauMuonSachCount", dashboardService.countYeuCauMuonSach());
        stats.put("nhanVienCount", dashboardService.countNhanVien());

        return stats;
    }
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
}
