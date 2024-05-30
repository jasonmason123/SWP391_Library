package com.springdemo.library.services;

import com.springdemo.library.model.User;
import com.springdemo.library.repositories.UserRepository;
import com.springdemo.library.security.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByTenUser(username).orElse(null);
        if(user == null)
            throw new UsernameNotFoundException(username);
        return new CustomUserDetails(user);
    }
}
