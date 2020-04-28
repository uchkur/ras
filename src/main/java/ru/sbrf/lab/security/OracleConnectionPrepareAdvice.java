package ru.sbrf.lab.security;
import oracle.jdbc.driver.OracleConnection;
import oracle.security.xs.NotAttachedException;
import oracle.security.xs.Session;
import oracle.security.xs.XSException;
import oracle.security.xs.XSSessionManager;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.jdbc.support.ConnectionUsernameProvider;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

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

    private Connection managerConnection = null;
    private XSSessionManager xsSessionManager = null;

    private Session currentSession;

//    @Autowired
//    private ConnectionUsernameProvider contextProvider;

    //    @AfterReturning(pointcut = "execution(* * (..))")
    public void logAroundAllMethods(){
        System.out.println("logit");
    }
//    @Pointcut("execution(* com.zaxxer.hikari.HikariDataSource.evictConnection(..))")
//    void beforeEvictConnectionPointcut() {};
//    @AfterReturning(pointcut = "beforeEvictConnectionPointcut()")
//
//    void afterEvidenceConnection(Connection connection) {
//        System.out.println("closed");
//        String prepString = String.format("{ call DBMS_SESSION.SET_IDENTIFIER('%s') }", contextProvider.getUserName());
//        CallableStatement cs = connection.prepareCall(prepString);
//        cs.execute();
//        cs.close();
//    }

    @Pointcut("execution(* javax.sql.DataSource.getConnection(..))")
    void prepareConnectionPointcut() {};
    @AfterReturning(pointcut="prepareConnectionPointcut()", returning = "connection")
    void afterPrepareConnection(Connection connection)  throws SQLException {
//        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        String currentUserName = "anonymous";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication==null))
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
             currentUserName = authentication.getName();
        }
        String prepString =
            String.format("{ call DBMS_SESSION.SET_IDENTIFIER('%s') }"
                , currentUserName );
        createSession(connection, currentUserName);
        CallableStatement cs = connection.prepareCall(prepString);
        cs.execute();
        cs.close();
    }

    private void createSession (Connection rasConnection, String user) throws SQLException
    {
        if (rasConnection.isClosed()) return;
        try {
            Connection managerConnection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521/aplcore"
                        , "sec_dispatcher"
                        , "secdispatcherpass");
            xsSessionManager = XSSessionManager.getSessionManager(managerConnection, 30, 8000000);
            if (user.equals("anonymous"))
            {
                this.currentSession = xsSessionManager.createAnonymousSession(
                        rasConnection.unwrap(OracleConnection.class),
                            null, null);
            }
            else {
                this.currentSession =
                        xsSessionManager.createSession(rasConnection.unwrap(OracleConnection.class)
                                , user
                                , null
                                , null
                        );
            }
            xsSessionManager.attachSession(rasConnection.unwrap(OracleConnection.class),
                    this.currentSession, null, null, null, null);
//todo make handler
        } catch (SQLException
                | XSException
                | NoSuchAlgorithmException
                | InvalidKeyException
                | InvalidKeySpecException
                | InvalidAlgorithmParameterException e)
        {
            try {
                if (!(this.currentSession == null) && !(this.xsSessionManager == null)) {
                    xsSessionManager.detachSession(this.currentSession);
                }
            }
            catch (XSException ee) {};
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            System.out.println(sw.toString());
        }
   }

}


