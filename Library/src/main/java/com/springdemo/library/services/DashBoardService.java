package com.springdemo.library.services;

import com.springdemo.library.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;



@Service
public class DashBoardService {

    @Autowired
    private SachRepository sachRepository;

    @Autowired
    private YeuCauMuonSachRepository yeuCauMuonSachRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NhanVienRepository nhanVienRepository;
    @Autowired
    private BlogRepository blogRepository;

    public Map<String, Long> getLoansByMonth() {
        Map<String, Long> data = new LinkedHashMap<>();
        List<Object[]> results = yeuCauMuonSachRepository.countLoansByMonth();
        for (Object[] result : results) {
            String month = "Tháng "+result[0].toString();
            Long count = (Long) result[1];
            data.put(month, count);
        }
        return data;
    }
    public Map<String, Long> getUserAccountByMonth() {
        Map<String, Long> data = new LinkedHashMap<>();
        List<Object[]> results = userRepository.countUserAccountByMonth();
        for (Object[] result : results) {
            String month = "Tháng "+result[0].toString();
            Long count = (Long) result[1];
            data.put(month, count);
        }
        return data;
    }
    public Map<String, Long> getBlogByMonth() {
        Map<String, Long> data = new LinkedHashMap<>();
        List<Object[]> results = blogRepository.countBlogByMonth();
        for (Object[] result : results) {
            String month = "Tháng "+result[0].toString();
            Long count = (Long) result[1];
            data.put(month, count);
        }
        return data;
    }
    public Map<String, Long> getStaffAccountByMonth() {
        Map<String, Long> data = new LinkedHashMap<>();
        List<Object[]> results = nhanVienRepository.countStaffAccountByMonth();
        for (Object[] result : results) {
            String month = "Tháng "+result[0].toString();
            Long count = (Long) result[1];
            data.put(month, count);
        }
        return data;
    }
    public long countUsers() {
        return userRepository.count();
    }

    public long countSach() {
        return sachRepository.count();
    }

    public long countYeuCauMuonSach() {
        return yeuCauMuonSachRepository.count();
    }
    public long countBoiThuong(){
        return yeuCauMuonSachRepository.countBoiThuong();
    }
    public long countDatCoc(){
        return yeuCauMuonSachRepository.countSoTienDatCoc();
    }
    public long countPhiMuonSach(){
        return yeuCauMuonSachRepository.countPhiMuonSach();
    }
    public long countNhanVien(){
        return nhanVienRepository.count();
    }
    public long countSachTrongKho(){
        return sachRepository.countSoLuongTrongKho();
    }

}

