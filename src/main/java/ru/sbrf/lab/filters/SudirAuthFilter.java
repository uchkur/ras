package ru.sbrf.lab.filters;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.sbrf.lab.security.SudirAuthenticationProvider;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.boot.context.properties.source.ConfigurationPropertyName.isValid;


@Component
public class SudirAuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        //todo migrate to JWT
//todo    final String requestTokenHeader = request.getHeader("Authorization");
//        String requestTokenHeader = request.getHeader("iv_user");
//        SudirAuthenticationProvider authenticationProvider = new SudirAuthenticationProvider();
//        Authentication auth = authenticationProvider.getAuthentication(requestTokenHeader);
//       SecurityContextHolder.getContext().setAuthentication(auth);
        //check
        System.out.println("CONTEXT:" + SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        filterChain.doFilter(request, response);
//        if(isValid(xAuth) == false){
//            throw new SecurityException();
//        }
        // The token is 'valid' so magically get a user id from it
//        Long id = getUserIdFromToken(xAuth);
/*
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            String username = ((UserDetails)principal).getUsername();
        } else {
            String username = principal.toString();
        }
        */
//        Authentication auth = jwtTokenProvider.getAuthentication(token);
//        SecurityContextHolder.getContext().setAuthentication(auth);
/*
        Map<String, String> map = new HashMap<String, String>();
        Enumeration headerNames = httpServletRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = httpServletRequest.getHeader(key);
            map.put(key, value);
        }
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        String url = "https://kbss.felk.cvut.cz/termit-server-dev/rest/users/current";
        HttpEntity<String> entity = new HttpEntity<>(map.toString(), headers);
        restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        */
    }
}

