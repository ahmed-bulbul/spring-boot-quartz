package com.bulbul.spring.quartz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SpringBootQuartzApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootQuartzApplication.class, args);
    }
}
