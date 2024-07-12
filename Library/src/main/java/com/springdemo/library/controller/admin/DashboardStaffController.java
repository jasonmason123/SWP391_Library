package com.springdemo.library.controller.admin;


import com.springdemo.library.repositories.BlogRepository;
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
public class DashboardStaffController {

    private final DashBoardService dashboardService;
    private final BlogRepository blogRepository;


    @GetMapping("/dashboard_staff")
    public ModelAndView viewDashboardStaff() {
        ModelAndView manageBookViewModel = new ModelAndView("admin_and_staff/Layout");
        manageBookViewModel.addObject("includedPage", "admin_and_staff/dashboard_staff");
        manageBookViewModel.addObject("title", "Dashboard Staff");
        return manageBookViewModel;
    }


    @GetMapping("/staff_stats")
    @ResponseBody
    public Map<String, Long> getStatsStaff() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("boiThuongCount", dashboardService.countBoiThuong());
        stats.put("datCocCount", dashboardService.countDatCoc());
        stats.put("yeuCauMuonSachCount", dashboardService.countYeuCauMuonSach());
        stats.put("phiMuonSachCount", dashboardService.countPhiMuonSach());

        return stats;
    }
    @PostMapping("/staff_areaChart")
    @ResponseBody
    public Map<String, Long> getStaffAreaChartData() {
        return dashboardService.getLoansByMonth();
    }
    @GetMapping("/staff_barChart")
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
