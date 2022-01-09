package com.example.matthew;


import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class AppConfig {

    @Bean
    //@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
    public MyRepository getRepository(){
        return new MyRepository();
    }

    @Bean
    @Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
    public MyService getService(){
        return new MyService(getRepository());
    }

}
