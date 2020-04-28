package ru.sbrf.lab.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

//todo import io.jsonwebtoken.Jwts;


import java.util.ArrayList;
import java.util.List;

@Component
public class SudirAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private SudirUserDetails sudirUserDetails;


    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        final List<GrantedAuthority> grantedAuths = new ArrayList<>();
        grantedAuths.add(new SimpleGrantedAuthority("PUBLIC"));
        final UserDetails principal = new User(name, password, grantedAuths);
        final Authentication auth = new UsernamePasswordAuthenticationToken(principal,
            password, grantedAuths);
        return auth;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
