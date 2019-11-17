package com.home.stat.config;

import service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
    @Bean
    public StatReporterService statReporterService() {
        return new StatReporterServiceImpl(statService());
    }

    @Bean
    public FileHandler fileHandler() {
        return new FileHandlerImpl(statService(), lineParser());
    }

    @Bean
    public StatService statService() {
        return new StatServiceImpl();
    }

    @Bean
    public LineParser lineParser() {
        return new LineParserImpl();
    }
}
