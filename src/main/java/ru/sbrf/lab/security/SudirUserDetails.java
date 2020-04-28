package ru.sbrf.lab.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SudirUserDetails implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//todo        final User user = userRepository.findByUsername(username);

//todo        if (user == null) {
//todo            throw new UsernameNotFoundException("User '" + username + "' not found");
//todo        }

        UserDetails user = User.withUsername(username)
                .password("{bcrypt}$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG")
                .roles("PUBLIC") //todo get roles from jwt
                .build();

        return user;
//        return org.springframework.security.core.userdetails.User//
//                .withUsername(username)//
//                .password("NULL")//
//                .authorities("PUBLIC")//
//                .accountExpired(false)//
//                .accountLocked(false)//
//                .credentialsExpired(false)//
//                .disabled(false)//
//                .build();
    }
}
