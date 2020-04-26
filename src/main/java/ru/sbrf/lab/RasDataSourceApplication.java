package ru.sbrf.lab;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
public class RasDataSourceApplication implements CommandLineRunner {
    public static void main(String... args) {
        SpringApplication.run(RasDataSourceApplication.class, args);
    }

    public void run(String... args) throws Exception {
        System.out.println("aaaaaa");

    }
}
