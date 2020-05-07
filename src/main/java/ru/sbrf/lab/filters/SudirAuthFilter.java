package ru.sbrf.lab.filters;


import oracle.security.xs.Session;
import oracle.security.xs.XSAuthenticationModule;
import oracle.security.xs.XSException;
import oracle.security.xs.XSSessionManager;
import oracle.security.xs.internal.SessionImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

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
import javax.sql.ConnectionPoolDataSource;


@Component
public class SudirAuthFilter extends OncePerRequestFilter {

    //@Autowired
    public Session currentSession;
    public XSSessionManager xsSessionManager;
    public Connection appConnection;
    public Connection managerConnection;
    private String jsessionid;
    private String previousJSessionId;
    private Set<String> jsessions = new HashSet<>();
    private Set<Session> sessions = new HashSet<>();

    public boolean isSecondCall(String jsessionid)
    {
        return  (jsessions.contains(jsessionid));
    }

    public String getJsessionid() {
        return jsessionid;
    }

    public void setJsessionid(String jsessionid) {
//        this.s;
        this.jsessions.add(jsessionid);
        this.sessions.add(this.currentSession);
//        this.previousJSessionId = this.jsessionid;
        this.jsessionid = jsessionid;
    }


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

        }

        System.out.println("CONTEXT BEFORE:" + SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        filterChain.doFilter(request, response);

        System.out.println("CONTEXT AFTER:" + SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        Iterator<Session> it = sessions.iterator();
        while (it.hasNext())
        {
            Session s = it.next();
            if (!(s==null)) {
                System.out.println("debug - detach " + ((SessionImpl) s).getSessionInfo().sessionCookie);
                try {
                    xsSessionManager.attachSession(this.appConnection,s, null, null,
                    null, null);
                    xsSessionManager.detachSession(s);

                    it.remove();
            } catch (XSException | SQLException e) {System.out.println(e.getMessage());}
            }
        }

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

