package com.example.matthew.config;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Profile("prod")
@Configuration
@ComponentScan(basePackages = "com.example.matthew")
@PropertySource("classpath:application-prod.properties")
public class ProdConfig {
}
