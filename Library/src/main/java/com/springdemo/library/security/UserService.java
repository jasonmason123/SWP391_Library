package com.springdemo.library.security;

import com.springdemo.library.model.User;
import com.springdemo.library.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByTenUser(username).orElse(null);
        if(user == null)
            throw new UsernameNotFoundException(username);
        return new CustomUserDetails(user);
    }
    public UserDetails loadUserById(int Id) throws UsernameNotFoundException {
        User user = userRepository.findById(Id).orElse(null);
        if(user == null)
            throw new UsernameNotFoundException("user not found: " + Id);
        return new CustomUserDetails(user);
    }
}
