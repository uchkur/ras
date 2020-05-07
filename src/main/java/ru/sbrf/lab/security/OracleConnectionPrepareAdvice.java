package ru.sbrf.lab.security;
import oracle.jdbc.driver.OracleConnection;
import oracle.security.xs.NotAttachedException;
import oracle.security.xs.Session;
import oracle.security.xs.XSException;
import oracle.security.xs.XSSessionManager;
import org.aspectj.lang.annotation.*;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.jdbc.support.ConnectionUsernameProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.sbrf.lab.filters.SudirAuthFilter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Aspect
@Component
public class OracleConnectionPrepareAdvice {
    @Autowired
    SudirAuthFilter sudirAuthFilter;

    private Connection managerConnection = null;
    private XSSessionManager xsSessionManager = null;

    //@Bean
    public Session getCurrentSession() {
        return currentSession;
    }

    public void setCurrentSession(Session currentSession) {
        this.currentSession = currentSession;
    }

    private Session currentSession;

//    @Autowired
//    private ConnectionUsernameProvider contextProvider;

//    @AfterReturning(pointcut = "execution(* javax.sql.DataSource.* (..))")
//    void closeConnectionPointcut() {
//        try {
//            this.xsSessionManager.destroySession(this.managerConnection, this.currentSession);
//            System.out.println("closeeeeeee");
//        } catch (XSException | SQLException e) {
//            System.out.println(e.getMessage());
//        }
//    }

    ;
//    public void logAroundAllMethods(){
//        System.out.println("logit");
//    }
//    @Pointcut("execution(* com.zaxxer.hikari.HikariDataSource.close(..))")
//    void beforeEvictConnectionPointcut() {};
//    @AfterReturning(pointcut = "beforeEvictConnectionPointcut()")
//
//    void afterEvidenceConnection(Connection connection) {
//        System.out.println("closed");
//        String prepString = String.format("{ call DBMS_SESSION.SET_IDENTIFIER('%s') }", contextProvidergetUserName());
//        CallableStatement cs = connection.prepareCall(prepString);
//        cs.execute();
//        cs.close();
//    }

    @Pointcut("execution(* javax.sql.DataSource.getConnection(..))")
    void prepareConnectionPointcut() {
    }

    ;

    @AfterReturning(pointcut = "prepareConnectionPointcut()", returning = "connection")
    void afterPrepareConnection(Connection connection) throws SQLException {
//        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        String currentUserName = "anonymous";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication == null))
            if (!(authentication instanceof AnonymousAuthenticationToken)) {
                currentUserName = authentication.getName();
            }
        String prepString =
                String.format("{ call DBMS_SESSION.SET_IDENTIFIER('%s') }"
                        , currentUserName);
        createSession(connection, currentUserName);
        sudirAuthFilter.xsSessionManager = xsSessionManager;
        sudirAuthFilter.appConnection = connection.unwrap(OracleConnection.class);
        CallableStatement cs = connection.prepareCall(prepString);
        cs.execute();
        cs.close();
    }

    private void createSession(Connection rasConnection, String user) throws SQLException {
        if (rasConnection.isClosed()) return;
        try {
            Connection managerConnection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521/aplcore"
                    , "sec_dispatcher"
                    , "secdispatcherpass");
            xsSessionManager = XSSessionManager.getSessionManager(managerConnection, 30, 8000000);
            if (user.equals("anonymous")) {
                if (sudirAuthFilter.getJsessionid() == null) {
                    sudirAuthFilter.setJsessionid("anonymous" + System.currentTimeMillis());
                    this.currentSession = xsSessionManager.createAnonymousSession(
                            rasConnection.unwrap(OracleConnection.class),
                            sudirAuthFilter.getJsessionid(), null);
                }
            } else {
                System.out.println("JSESSION - " + sudirAuthFilter.getJsessionid());
                if (!sudirAuthFilter.isSecondCall(sudirAuthFilter.getJsessionid())) {
                    this.currentSession =
                            xsSessionManager.createSession(rasConnection.unwrap(OracleConnection.class)
                                    , user
                                    , sudirAuthFilter.getJsessionid()
                                    , null
                            );
                }
            }
            xsSessionManager.attachSessionByCookie(rasConnection.unwrap(OracleConnection.class),
                    sudirAuthFilter.getJsessionid(),
                    null, null, null,
                    null, null);
            sudirAuthFilter.currentSession = this.currentSession;
            sudirAuthFilter.setJsessionid(sudirAuthFilter.getJsessionid());
            sudirAuthFilter.managerConnection = this.managerConnection;
            managerConnection.close();
//            xsSessionManager.setCookie(this.currentSession, this.sudirAuthFilter.jsessionid);
            System.out.println("debug 2 - session is attached?:" + currentSession.isAttached());

//todo make handler
        } catch (SQLException
                | XSException
                | NoSuchAlgorithmException
                | InvalidKeyException
                | InvalidKeySpecException
                | InvalidAlgorithmParameterException
                e) {
            try {
                if (!(this.currentSession == null) && !(this.xsSessionManager == null)) {
                    xsSessionManager.detachSession(this.currentSession);
                }
            } catch (XSException ee) {
            }
            ;
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            System.out.println(sw.toString());
        }
    }
}




