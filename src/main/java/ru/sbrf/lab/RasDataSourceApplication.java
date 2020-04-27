package ru.sbrf.lab;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "ru.sbrf.lab.repository")
@EnableTransactionManagement
public class RasDataSourceApplication implements CommandLineRunner {
    public static void main(String... args) {
        SpringApplication.run(RasDataSourceApplication.class, args);
    }

    public void run(String... args) throws Exception {
        System.out.println("lab.sbrf.ru - listening");

    }
}
