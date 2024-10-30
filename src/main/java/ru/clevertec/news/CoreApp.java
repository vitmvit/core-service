package ru.clevertec.news;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class CoreApp {

    public static void main(String[] args) {
        SpringApplication.run(CoreApp.class, args);
    }
}