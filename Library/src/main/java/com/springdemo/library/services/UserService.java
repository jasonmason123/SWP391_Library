package com.springdemo.library.services;

import com.springdemo.library.model.NhanVien;
import com.springdemo.library.model.User;
import com.springdemo.library.repositories.NhanVienRepository;
import com.springdemo.library.repositories.UserRepository;
import com.springdemo.library.security.userdetails.NhanVienUserDetails;
import com.springdemo.library.security.userdetails.CustomUserDetails;
import lombok.Builder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    private UserRepository userRepository;
    private NhanVienRepository nhanVienRepository;

    @Builder
    public UserService(UserRepository userRepository, NhanVienRepository nhanVienRepository) {
        this.userRepository = userRepository;
        this.nhanVienRepository = nhanVienRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByTenUser(username).orElse(null);
        if(user == null)
            throw new UsernameNotFoundException(username);
        return new CustomUserDetails(user);
    }

    public UserDetails loadNhanVienByTenNhanVien(String tenNhanVien) throws UsernameNotFoundException {
        NhanVien nhanVien = nhanVienRepository.findNhanVienByTenNhanVien(tenNhanVien).orElse(null);
        if(nhanVien == null)
            throw new UsernameNotFoundException(tenNhanVien);
        return new NhanVienUserDetails(nhanVien);
    }
}
