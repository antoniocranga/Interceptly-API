package com.interceptly.api.util.security;

import com.interceptly.api.dao.UserDao;
import com.interceptly.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserDao> userDao = userRepository.findByEmail(email);
        if(userDao.isEmpty()){
            throw new UsernameNotFoundException("Not found!");
        }
        return User
                .withUsername(userDao.get().getEmail())
                .password(userDao.get().getPassword())
                .authorities("erasmus")
                .build();
    }
}
