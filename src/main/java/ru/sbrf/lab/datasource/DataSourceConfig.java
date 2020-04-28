package ru.sbrf.lab.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import oracle.jdbc.pool.OracleDataSource;
import oracle.security.xs.XSException;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;
@Configuration
@EnableTransactionManagement
//@EnableJpaRepositories(
//        entityManagerFactoryRef = "rasEntityManagerFactory", basePackages = { "ru.sbrf.lab.repository"})

public class DataSourceConfig {
//    @Primary
//    @Bean
//    @ConfigurationProperties("ras.datasource")
//    public DataSourceProperties dataSourceProperties() {
//        return new DataSourceProperties();
//    }
    @Primary
    @Bean(destroyMethod = "close", name = "dataSource")
    @ConfigurationProperties("ras.datasource")
    public HikariDataSource dataSource (DataSourceProperties properties) throws SQLException {
//todo session attach
        return properties.initializeDataSourceBuilder().type(HikariDataSource.class)
        .build();
        /*
        OracleDataSource dataSource = new OracleDataSource();
        dataSource.setUser("apl_direct");
        dataSource.setPassword("apldirectpas");
        dataSource.setURL("jdbc:oracle:thin:@localhost:1521/aplcore");
        HikariConfig config = new HikariConfig();
        config.setDataSource(dataSource);
        config.setPoolName("RAS_POOL");
        config.setConnectionTimeout(3000);
        config.setMaximumPoolSize(100);
        config.setJdbcUrl("jdbc:oracle:thin:@localhost:1521/aplcore");
        return new HikariDataSource (config);
        */
    }
}
