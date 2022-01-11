package com.example.matthew;


import org.springframework.context.annotation.*;

@Profile("local")
@Configuration
@ComponentScan(basePackages = "com.example.matthew")
@PropertySource("classpath:application.properties")
public class AppConfig {

}
