package ru.sbrf.lab.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.sql.SQLException;
@Configuration
@EnableTransactionManagement
//@EnableJpaRepositories(
 //       entityManagerFactoryRef = "rasEntityManagerFactory", basePackages = {
 //       "ru.sbrf.lab.repository", "ru.sbrf.lab.datasource"})

public class DataSourceConfig {
    @Primary
    @Bean
    @ConfigurationProperties("ras.datasource")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }
    @Primary
    @Bean(destroyMethod = "close", name = "dataSource")
    @ConfigurationProperties("ras.datasource")
    public RasDataSource dataSource(DataSourceProperties properties) {
//todo session attach
        return properties.initializeDataSourceBuilder().type(RasDataSource.class)
                .build();
    }
}
