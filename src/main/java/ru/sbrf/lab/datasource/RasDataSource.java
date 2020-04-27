package ru.sbrf.lab.datasource;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.SQLException;

@Configuration
public class RasDataSource extends HikariDataSource {

    @Override
    public Connection getConnection() throws SQLException {
        Connection conn = super.getConnection();
        return RasConnection.newInstance(conn); //proxy
    }

    @Override
    public Connection getConnection(String user, String pass) throws SQLException {
        Connection conn = super.getConnection(user, pass);
        return RasConnection.newInstance(conn); //proxy
    }
}

