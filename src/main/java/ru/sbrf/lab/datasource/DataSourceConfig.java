package ru.sbrf.lab.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

import java.sql.SQLException;
@Configuration
public class DataSourceConfig {
//    @Primary
//    @Bean
//    @ConfigurationProperties("app.datasource")
//    public DataSourceProperties dataSourceProperties() {
//        return new DataSourceProperties();
//    }
//    @Primary
//    @Bean(destroyMethod = "close", name = "dataSource")
//    @ConfigurationProperties("app.datasource")
//    public HikariDataSource dataSource(DataSourceProperties properties) {
//        return properties.initializeDataSourceBuilder().type(HikariDataSource.class)
//                .build();
//    }

    @Primary
    @Bean(destroyMethod = "close", name = "dataSource")

    public HikariDataSource dataSource() throws SQLException {
        RasDataSource dataSource = new RasDataSource();
        dataSource.setUsername("apl_security");
        dataSource.setPassword("schemapas");
        dataSource.setJdbcUrl("jdbc:oracle:thin:@localhost:1521/aplcore");
        HikariConfig config = new HikariConfig();
        config.setDataSource(dataSource);
        config.setPoolName("MY_POOL");
        return new HikariDataSource(config);
    }
}
