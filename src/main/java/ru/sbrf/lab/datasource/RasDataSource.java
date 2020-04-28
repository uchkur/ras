package ru.sbrf.lab.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import oracle.jdbc.driver.OracleConnection;
import oracle.jdbc.pool.OracleDataSource;
import oracle.security.xs.Session;
import oracle.security.xs.XSException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import oracle.security.xs.XSSessionManager;
import oracle.jdbc.pool.OracleDataSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
@ConfigurationProperties("ras.datasource")
public class RasDataSource extends HikariDataSource {
    @Value("${ras.datasource.dlau_dispatcher_user}")
    private String DLAU_DISPATCHER_USER="sec_dispatcher";
    @Value("${ras.datasource.dlau_dispatcher_user_password}")
    private String DLAU_DISPATCHER_USER_PASSWORD="secdispatcherpass";
    @Value("${ras.datasource.dispatcher_url}")
    private String DISPATCHER_URL = "jdbc:oracle:thin:@localhost:1521/aplcore";

    @Autowired
    private DataSourceProperties dataSourceProperties;

    private Connection managerConnection = null;
    private XSSessionManager xsSessionManager = null;

    private Session currentSession;

//    public RasDataSource() throws SQLException {
//        try {
//            this.managerConnection = getManagerConnection();
//        }
//        catch (SQLException e) {
//            StringWriter sw = new StringWriter();
//            PrintWriter pw = new PrintWriter(sw);
//            e.printStackTrace(pw);
//            System.out.println(sw.toString());
//        }
//        this.xsSessionManager = getXsSessionManager(managerConnection);
//    }

    private Connection getManagerConnection() throws  SQLException {
        return (DriverManager.getConnection(DISPATCHER_URL
                , DLAU_DISPATCHER_USER
                , DLAU_DISPATCHER_USER_PASSWORD));
    }


    private XSSessionManager getXsSessionManager(Connection conn){
        try {
            xsSessionManager = XSSessionManager.getSessionManager(conn, 30, 8000000);
//todo make handler
        } catch (SQLException
                | XSException
                | NoSuchAlgorithmException
                | InvalidKeyException
                | InvalidKeySpecException
                | InvalidAlgorithmParameterException e)
        {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            System.out.println(sw.toString());
        }
        return xsSessionManager;

    }
    private void createSession (Connection rasConnection)
    {
        try {
            this.currentSession =
                    xsSessionManager.createSession (rasConnection.unwrap(OracleConnection.class)
//todo                            , SecurityContextHolder.getContext().getAuthentication().getName()
                            , "TITOV_RV"
                            , null
                            , null
                    );
//todo make handler
        } catch (SQLException
                | XSException e
                )
        {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            System.out.println(sw.toString());
        }
    }

  //  @Bean
   // @Override
    public Connection getConnection() throws SQLException {
        Connection conn = super.getConnection();
        Connection rasConnection = RasConnection.newInstance(conn);
        createSession(rasConnection);
        return rasConnection; //proxy
    }
//    @Bean
    //@Override
    public Connection getConnection(String user, String pass) throws SQLException {
        Connection conn = super.getConnection(user, pass);
        Connection rasConnection = RasConnection.newInstance(conn);
        createSession(rasConnection);
        return rasConnection; //proxy
    }
}

