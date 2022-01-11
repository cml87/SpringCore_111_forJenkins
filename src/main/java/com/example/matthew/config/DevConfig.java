package com.example.matthew.config;


import org.springframework.context.annotation.*;

@Profile("dev")
@Configuration
@ComponentScan(basePackages = "com.example.matthew")
@PropertySource("classpath:application-dev.properties")
public class DevConfig {
}
