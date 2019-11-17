package com.home.stat;

import service.FileHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LogStatApplication {
    public static void main(String[] args) {
        var context = SpringApplication.run(LogStatApplication.class, args);
        context.getBean("fileHandler", FileHandler.class).handle();
    }
}
