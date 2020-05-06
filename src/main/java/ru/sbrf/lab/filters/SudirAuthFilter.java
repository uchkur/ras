package ru.sbrf.lab.filters;

import oracle.jdbc.driver.OracleConnection;
import oracle.security.xs.NotAttachedException;
import oracle.security.xs.Session;
import oracle.security.xs.XSException;
import oracle.security.xs.XSSessionManager;
import oracle.security.xs.internal.SessionImpl;
import org.hibernate.internal.util.xml.XsdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.sbrf.lab.security.APLSession;
import ru.sbrf.lab.security.SudirAuthenticationProvider;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.boot.context.properties.source.ConfigurationPropertyName.isValid;


@Component
public class SudirAuthFilter extends OncePerRequestFilter {

    //@Autowired
    public Session currentSession;
    public XSSessionManager xsSessionManager;
    public Connection appConnection;
    public String jsessionid;

//    private void createSession (String user) throws SQLException
//    {
//        try {
//
////            Connection managerConnection = DriverManager.getConnection(
////                    "jdbc:oracle:thin:@localhost:1521/aplcore"
////                    , "sec_dispatcher"
////                    , "secdispatcherpass");
////            xsSessionManager = XSSessionManager.getSessionManager(managerConnection, 30, 8000000);
////            appConnection = DriverManager.getConnection(
////                    "jdbc:oracle:thin:@localhost:1521/aplcore"
////                    , "apl_direct"
////                    , "apldirectpas");
//
////            currentSession = new APLSession (
////                    xsSessionManager.createSession
////                            (appConnection, user, null, null));
////            xsSessionManager.attachSession(appConnection,
////                    currentSession.getRasSession(), null, null, null, null);
////            xsSessionManager.detachSession(this.currentSession);
//    } catch (SQLException
//                | XSException
////                | NoSuchAlgorithmException
// //               | InvalidKeyException
//  //              | InvalidKeySpecException
//   //             | InvalidAlgorithmParameterException
//        e)
//        {
//            StringWriter sw = new StringWriter();
//            PrintWriter pw = new PrintWriter(sw);
//            e.printStackTrace(pw);
//            System.out.println(sw.toString());
//        };
//    }

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

        Cookie[] allCookies = request.getCookies();
        if (allCookies != null) {
            Cookie session =
                    Arrays.stream(allCookies).filter(x -> x.getName().equals("JSESSIONID"))
                            .findFirst().orElse(null);
            jsessionid = session.getValue();

//            if (session != null) {
//
//                session.setHttpOnly(true);
//                session.setSecure(true);
//                res.addCookie(session);
//            }
        }

        System.out.println("CONTEXT BEFORE:" + SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        try {
            //createSession(SecurityContextHolder.getContext().getAuthentication().getName());
            System.out.println(currentSession.toString());
        }
        catch (Exception e) {

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            System.out.println(sw.toString());

        };
        filterChain.doFilter(request, response);
        System.out.println("CONTEXT AFTER:" + SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        try {
            xsSessionManager.detachSession(currentSession);
            System.out.println(currentSession.toString());
        }
        catch (XSException|SQLException e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            System.out.println(sw.toString());
        }

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

