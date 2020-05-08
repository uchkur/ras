package ru.sbrf.lab.filters;


import oracle.security.xs.Session;
import oracle.security.xs.XSException;
import oracle.security.xs.XSSessionManager;
import oracle.security.xs.internal.SessionImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;


@Component
public class SudirAuthFilter extends OncePerRequestFilter {


    //@Autowired
    public Session currentSession;
    public XSSessionManager xsSessionManager;
    public Connection appConnection;
    public Connection managerConnection;
    private String jsessionid;
    private String iv_user;

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
        String user = request.getHeader("iv-user");
        String tmp = user.trim().toUpperCase();
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e)
        {
            System.out.println(e.getMessage());
        }
        md.update(tmp.getBytes());
        byte[] digest = md.digest();
        String ras_user = "APL_" +  DatatypeConverter.printHexBinary(digest).toUpperCase();
        System.out.println("RAS USER !!!!!!!!!!!!!");
        System.out.println(ras_user);

//        String token = (StringUtils.isBlank(header_authorization) ? null : header_authorization.split(" ")[1]);
        UserDetails principal = new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public String getPassword() {
                return ras_user;
            }

            @Override
            public String getUsername() {
                return user;
            }

            @Override
            public boolean isAccountNonExpired() {
                return false;
            }

            @Override
            public boolean isAccountNonLocked() {
                return false;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return false;
            }

            @Override
            public boolean isEnabled() {
                return false;
            }
        };

        UsernamePasswordAuthenticationToken userAuthenticationToken = new UsernamePasswordAuthenticationToken(
                principal, "", principal.getAuthorities());
        getContext().setAuthentication(userAuthenticationToken);
        System.out.println(getContext().getAuthentication().getName());




        Enumeration<String> headers  = request.getHeaderNames();
        while (headers.hasMoreElements())
        {
            String header_name = headers.nextElement();
            System.out.println(header_name);
        }


        Cookie[] allCookies = request.getCookies();
        if (allCookies != null) {
            Cookie session =
                    Arrays.stream(allCookies).filter(x -> x.getName().equals("JSESSIONID"))
                            .findFirst().orElse(null);
            jsessionid = session.getValue();

        }

        System.out.println("CONTEXT BEFORE:" + getContext().getAuthentication().getPrincipal());

        filterChain.doFilter(request, response);

        System.out.println("CONTEXT AFTER:" + getContext().getAuthentication().getPrincipal());

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

